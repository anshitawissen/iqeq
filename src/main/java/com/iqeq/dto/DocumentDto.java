package com.iqeq.dto;

import com.iqeq.model.Document;
import com.iqeq.util.CommonConstants;
import jakarta.persistence.Tuple;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DocumentDto extends BaseDto {

    private String name;
    private String batchName;
    private String priority;
	private StatusDto status;
    private Long documentTypeId;
    private String documentType;
	private String documentSubType;
    private String parentDocumentName;
    private Boolean isBatch;
    private Long documentBatchId;
    private Long id;
    private Float successMatrix;
    private String processingDateTime;
    private Long parentDocumentId;
    private String path;
	private String priorityColor;
	private String processingTime;

	public static DocumentDto build(Document document) {
		DocumentDto dto = new DocumentDto();
		dto.setId(document.getDocumentId());
		dto.setCreatedBy(document.getCreatedBy());
		dto.setCreatedDate(document.getCreatedDate());
		dto.setPriority(document.getPriority().getLabel());
		dto.setPriorityColor(document.getPriority().getColorCode());
		dto.setDocumentTypeId(document.getDocumentType().getDocumentTypeId());
		dto.setName(document.getName());
		dto.setIsBatch(document.getDocumentBatch() != null);
		if (document.getDocumentBatch() != null) {
			dto.setDocumentBatchId(document.getDocumentBatch().getDocumentBatchId());
			dto.setBatchName(document.getDocumentBatch().getName());
		}
		if(Objects.nonNull(document.getDocumentType())){
			dto.setDocumentType(document.getDocumentType().getName());
			dto.setDocumentTypeId(document.getDocumentType().getDocumentTypeId());
		}
		if(Objects.nonNull(document.getDocumentSubType())){
			dto.setDocumentSubType(document.getDocumentSubType().getName());
		}
		dto.setSuccessMatrix(document.getSuccessMatrix());
		if (document.getProcessingEndAt() != null) {
			dto.setProcessingDateTime(document.getProcessingEndAt().format(CommonConstants.DATE_TIME_FORMATTER));
		}
		if (document.getProcessingStartAt() != null && document.getProcessingEndAt() != null) {
			Duration duration = Duration.between(document.getProcessingStartAt(), document.getProcessingEndAt());
			long minutes = duration.toMinutes();
			long seconds = duration.toSecondsPart(); // Directly gets remaining seconds
			if (minutes > 0) {
				dto.setProcessingTime(minutes + " min " + seconds + " sec");
			} else {
				dto.setProcessingTime(seconds + " sec");
			}
		}else {
			dto.setProcessingTime("0 sec");
		}
		return dto;
    }

	public static DocumentDto build(Tuple tuple) {
		Document document = tuple.get("document", Document.class);
		DocumentDto dto = build(document);

		StatusDto status = new StatusDto(
				tuple.get("statusValue", String.class),
				tuple.get("statusColor", String.class),
				tuple.get("statusIcon", String.class),
				tuple.get("statusUiSequence", Integer.class)
		);
		dto.setStatus(status);
		return dto;
	}
}
