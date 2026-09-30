package com.BTA.SmartPrep.domain.entity;

import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubmissionAnswerStorageTest {
    @Test
    void answerEntityColumnUsesText() throws NoSuchFieldException {
        Field answer = Submission.class.getDeclaredField("answer");
        Column column = answer.getAnnotation(Column.class);

        assertEquals("TEXT", column.columnDefinition());
    }

    @Test
    void submissionsSchemaStoresAnswerAsText() throws Exception {
        String schema = Files.readString(Path.of("src/main/resources/db/schema.sql"));
        Matcher table = Pattern.compile("CREATE TABLE `Submissions` \\((.*?)\\n\\)", Pattern.DOTALL)
                .matcher(schema);

        assertTrue(table.find(), "Submissions table definition must exist");
        assertTrue(Pattern.compile("`answer`\\s+text\\b", Pattern.CASE_INSENSITIVE)
                .matcher(table.group(1)).find(), "Submissions.answer must use TEXT");
    }
}
