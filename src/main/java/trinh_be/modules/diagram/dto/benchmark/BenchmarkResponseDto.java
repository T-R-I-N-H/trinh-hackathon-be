package trinh_be.modules.diagram.dto.benchmark;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BenchmarkResponseDto {

    @JsonProperty("benchmark_data")
    private Map<String, String> benchmarkData;

    public BenchmarkResponseDto(BenchmarkAIResponseDto response) {
        this.benchmarkData = response.getBenchmarkData();
    }
}
