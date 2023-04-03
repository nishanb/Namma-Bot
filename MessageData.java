package com.example.workflow.service;

import org.h2.message.DbException;

public interface MessageData {

   // String formatMessage(String templateId, String language, String variables) throws Exception;

    String formatMessage(String templateId, String language, String[] variables) throws Exception;
}
