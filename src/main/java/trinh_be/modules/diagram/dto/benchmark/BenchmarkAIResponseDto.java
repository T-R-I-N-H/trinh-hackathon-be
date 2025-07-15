package trinh_be.modules.diagram.dto.benchmark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class BenchmarkAIResponseDto {

    @JsonProperty("benchmark_data")
    private Map<String, String> benchmarkData;
}
