package org.example.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileDto {
    Integer id;
    String fileName;
    String fileType;
    String fileDownloadUri;
}
