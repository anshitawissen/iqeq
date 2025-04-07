-- Document Type

INSERT INTO public.document_type(
    document_type_id, created_by, created_date, revision_no, updated_by, updated_date, is_archive, name, value
)
VALUES
    (nextval('document_type_seq'), 'abc', CURRENT_TIMESTAMP, 0, 'abc', CURRENT_TIMESTAMP, false, 'invoice', 'Invoice'),
    (nextval('document_type_seq'), 'abc', CURRENT_TIMESTAMP, 0, 'abc', CURRENT_TIMESTAMP, false, 'letterOfCredit', 'Letter Of Credit'),
    (nextval('document_type_seq'), 'abc', CURRENT_TIMESTAMP, 0, 'abc', CURRENT_TIMESTAMP, false, 'landContract', 'Land Contract');

-- Document Statuses

INSERT INTO public.document_status(
	document_status_id, created_by, created_date, is_archive, revision_no, updated_by, updated_date, color_code, icon_name, label, ui_sequence, value)
	VALUES
	(nextval('document_status_seq'), 'abc',CURRENT_TIMESTAMP, false, 0, 'abc', CURRENT_TIMESTAMP, '#10a142', 'BsClipboard2CheckFill', 'PROCESSED', 2,'Processed'),
	(nextval('document_status_seq'), 'abc',CURRENT_TIMESTAMP, false, 0, 'abc', CURRENT_TIMESTAMP, '#ca533e', 'PiHourglassSimpleFill', 'PENDING_EXTRACTION', 4,'Pending Extraction'),
	(nextval('document_status_seq'), 'abc',CURRENT_TIMESTAMP, false, 0, 'abc', CURRENT_TIMESTAMP, '#f29425', 'GrPowerCycle', 'IN_PROGRESS', 3,'In Progress'),
	(nextval('document_status_seq'), 'abc',CURRENT_TIMESTAMP, false, 0, 'abc', CURRENT_TIMESTAMP, '#3e8ecf', 'IoPerson', 'PENDING_MANUAL_QA', 5,'Pending Manual QA'),
	(nextval('document_status_seq'), 'abc',CURRENT_TIMESTAMP, false, 0, 'abc', CURRENT_TIMESTAMP, '#3ecabe', 'BsClockFill', 'PENDING_VALIDATION', 6,'Pending Validation');