package de.exxcellent.challenge.reader.serviceImpl;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.reader.service.TableFileReader;
import org.apache.tika.Tika;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CSV implementation of {@link TableFileReader}.
 *
 * @param <T> the record type produced by this reader
 */
public class TableFileReaderCsvImpl<T> implements TableFileReader<T> {

    private final Reader reader;
    private final Class<T> clazz;
    private final CsvToBean<T> csvToBean;
    private final Iterator<T> iterator;

    public TableFileReaderCsvImpl(String filePath, Class<T> clazz) {
        this.clazz = clazz;

        validateCsvFile(filePath);
        validateCsvHeaders(filePath);

        try {
            this.reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw AppException.fileNotFound(filePath, e);
        }

        this.csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                .withIgnoreLeadingWhiteSpace(true)
                .withThrowExceptions(false)
                .build();

        this.iterator = csvToBean.iterator();
    }

    @Override
    public Optional<T> read() {
        if (iterator.hasNext()) {
            T record = iterator.next();
            return Optional.of(record);
        }
        return Optional.empty();
    }

    @Override
    public List<AppException> getFailedRowsExceptions() {
        ArrayList<AppException> appExceptionList = new ArrayList<>();
        List<CsvException> capturedExceptions = csvToBean.getCapturedExceptions();
        for (CsvException ex : capturedExceptions) {
            appExceptionList.add(AppException.malformedRow(ex.getLineNumber(), ex));
        }
        return appExceptionList;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw AppException.fileCloseFailure(e);
        }
    }

    /**
     * Extracts required header names from the target class by inspecting.
     *
     * @param clazz the target class to inspect
     * @return set of required column names, may be empty if none are required
     */
    private static Set<String> getRequiredHeadersFromClass(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(CsvBindByName.class))
                .map(f -> f.getAnnotation(CsvBindByName.class))
                .filter(CsvBindByName::required)
                .map(CsvBindByName::column)
                .filter(col -> !col.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Validates that the file exists, is not a directory, is readable, and has a CSV-compatible MIME type.
     *
     * @param filePath path to the file to validate
     */
    private void validateCsvFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            throw AppException.fileNotFound(filePath, null);
        }
        if (file.isDirectory()) {
            throw AppException.fileNotFound(filePath, null);
        }
        if (!file.canRead()) {
            throw AppException.fileNotReadable(filePath, null);
        }

        Tika tika = new Tika();
        try {
            String mime = tika.detect(new File(filePath));
            mime = mime == null ? "" : mime.toLowerCase(Locale.ROOT).trim();

            int semicolon = mime.indexOf(';');
            if (semicolon >= 0) mime = mime.substring(0, semicolon).trim();

            boolean allowed =
                    mime.startsWith("text/") ||
                            mime.equals("application/csv") ||
                            mime.equals("text/csv") ||
                            mime.equals("application/vnd.ms-excel");

            if (!allowed) {
                throw AppException.fileNotReadable(filePath, null);
            }
        } catch (IOException e) {
            throw AppException.fileNotReadable(filePath, e);
        }
    }

    /**
     * Validates that the CSV file contains all required headers.
     *
     * @param filePath path to the CSV file to validate
     */
    private void validateCsvHeaders(String filePath) {
        Set<String> requiredHeaders = getRequiredHeadersFromClass(clazz);

        if (requiredHeaders.isEmpty()) {
            return;
        }

        String[] headers;
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            headers = csvReader.readNext();
        } catch (IOException | CsvValidationException e) {
            throw AppException.fileNotReadable(filePath, e);
        }

        if (headers == null) {
            String missingHeaders = String.join(", ", requiredHeaders);
            throw AppException.missingColumn(missingHeaders);
        }

        Set<String> actualHeaders = Arrays.stream(headers)
                .map(String::trim)
                .collect(Collectors.toSet());

        requiredHeaders.removeAll(actualHeaders);
        String missingHeaders = String.join(", ", requiredHeaders);

        if (!requiredHeaders.isEmpty()) {
            throw AppException.missingColumn(missingHeaders);
        }
    }

}
