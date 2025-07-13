package trinh_be.common.s3;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import trinh_be.utils.SpringContextUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileService {

    private final S3Client s3Client;
    private final String BUCKET_NAME = "vpbankhackathon-s3bucket";

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID()+ "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        Dotenv dotenv = Dotenv.load();
        String region = dotenv.get("S3_REGION");

        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return "https://" + BUCKET_NAME + ".s3." + region + ".amazonaws.com/" + encodedKey;
    }

    public static S3FileService getInstance() {
        return SpringContextUtils.getSingleton(S3FileService.class);
    }
}
