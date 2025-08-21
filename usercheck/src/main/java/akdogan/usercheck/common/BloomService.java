package akdogan.usercheck.common;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

@Component
public class BloomService {
    private final BloomFilter<String> bloomFilter;

    // Default values
    private static final int DEFAULT_EXPECTED_INSERTIONS = 1_000_000;
    private static final double DEFAULT_FALSE_POSITIVE_PROBABILITY = 0.01;

    public BloomService(){
        this(DEFAULT_EXPECTED_INSERTIONS,DEFAULT_FALSE_POSITIVE_PROBABILITY);
    }

    public BloomService(int expectedInsertions, double falsePositiveProbability) {
        this.bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                expectedInsertions,
                falsePositiveProbability
        );
    }

    public void add(String value){
        this.bloomFilter.put(value);
    }
    public boolean mightContain(String value){
        return this.bloomFilter.mightContain(value);
    }
}