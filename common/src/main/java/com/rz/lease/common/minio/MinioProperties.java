package com.rz.lease.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String endpoint;
    private String publicEndpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public String toPublicUrl(String url) {
        if (url == null || endpoint == null || bucketName == null) {
            return url;
        }
        String internalBase = endpoint.replaceFirst("/+$", "");
        String bucketPrefix = internalBase + "/" + bucketName + "/";
        if (!url.startsWith(bucketPrefix)) {
            return url;
        }
        String publicBase = publicEndpoint == null || publicEndpoint.isBlank()
                ? internalBase
                : publicEndpoint.replaceFirst("/+$", "");
        return publicBase + url.substring(internalBase.length());
    }

}
