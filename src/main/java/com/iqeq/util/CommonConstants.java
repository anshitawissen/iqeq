package com.iqeq.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommonConstants {

	public static final String SUCCESS_MSG = "success";
	public static final String ERROR_MSG = "error";

	public static final String STATUS_ALL = "All";
	public static final int UI_SEQUENCE_ALL = 1;
	public static final String ICON_NAME_ALL = "LuFolderCheck";
	public static final String COLOR_CODE_ALL = "#343434";

	public static final String STATUS_COUNT_SUCCESS = "Status count send successfully";
	public static final String DOCUMENT_SUB_TYPE_SUCCESS = "Document sub type added successfully";
	public static final String GET_DOCUMENT_SUB_TYPE_SUCCESS = "Document sub type by document type fetched successfully";
	public static final String GET_PRIORITIES_SUCCESS = "Priorities fetched successfully";
	public static final String CREATE_PRIORITIES_SUCCESS = "Priority Saved successfully";

	public static final String PENDING_EXTRACTION = "PENDING_EXTRACTION";

	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
	public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	public static final String EXPORT_DOCUMENT_NAME = "Documents.xlsx";

	public static final String EXPORT_AUDIT_DOCUMENT_NAME = "Audit_Documents.xlsx";

	public static final String EXPORT_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static final String STATUS = "Status";
	public static final String REVISION_NUMBER = "Revision Number";
	public static final String CREATED_DATE = "Created Date";
	public static final String CREATED_BY = "Created By";
	public static final String UPDATED_DATE = "Updated Date";
	public static final String UPDATED_BY = "Updated By";
	public static final String Is_Archived = "Is Archived";


	public static final List<String> EXPORT_DOCUMENT_HEADERS = List.of("Document Name", "Batch Name", "Priority Order", "Updated Date",
			"Processed Date Time", "Accuracy", "Status");

	public static final List<String> EXPORT_DOCUMENT_FIELDS = List.of("name", "batchName", "priority", "updatedDate",
			"processingDateTime", "successMatrix", "status");

	public static final List<String> AUDIT_EXPORT_DOCUMENT_HEADERS = List.of(
			"Document ID", "Document Name", "Document Batch Name", "Priority Order", STATUS, "Accuracy",
			"Processing Date Time", "Document Type Name", "File Path", "Parent Document Name", CREATED_DATE, UPDATED_DATE, CREATED_BY, UPDATED_BY,
			Is_Archived, REVISION_NUMBER
	);

	public static final List<String> AUDIT_EXPORT_DOCUMENT_FIELDS = List.of(
			"id", "name", "batchName", "priority", "status", "successMatrix",
			"processingDateTime", "typeName", "path", "parentDocumentName","createdDate", "updatedDate", "createdBy", "updatedBy",
			"isArchive", "revisionNo"
	);

	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	public static final String FORWARD_SLASH = "/";


	private CommonConstants() { throw new IllegalStateException("Utility class");}
}
