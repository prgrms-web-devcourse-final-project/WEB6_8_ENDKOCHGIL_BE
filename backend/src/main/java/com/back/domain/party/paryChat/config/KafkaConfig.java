package com.back.domain.party.paryChat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    private static final long INTERVAL_MS = 1000L; // 재처리 간격 1초
    private static final long MAX_ATTEMPTS = 3L;   // 최대 재처리 횟수 (총 3회 시도)

    /**
     * Kafka Consumer에서 메시지 처리 실패 시 재처리 및 최종 복구 로직을 정의하는 에러 핸들러를 Bean으로 등록합니다.
     */
    @Bean
    public DefaultErrorHandler errorHandler() {
        // 1. 재처리 정책 설정: 1초 간격으로 2번 재시도 (총 3회 시도)
        FixedBackOff fixedBackOff = new FixedBackOff(INTERVAL_MS, MAX_ATTEMPTS - 1);

        // 2. 최종 실패 시 복구 로직 정의 (ConsumerRecordRecoverer 사용)
        ConsumerRecordRecoverer recoverer = (record, exception) -> {
            // 메시지 처리 실패 시 로그를 남기는 복구 로직
            System.err.printf(
                    "--- FINAL FAILURE --- Message could not be processed after %d retries. Topic: %s, Partition: %d, Offset: %d, Key: %s, Exception: %s%n",
                    MAX_ATTEMPTS,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key(),
                    exception.getMessage()
            );

            // TODO: (선택 사항) 여기에 Dead Letter Queue(DLQ)로 메시지를 발행하는 로직을 추가하여 영구적인 메시지 유실을 방지해야 합니다.
        };

        // 3. DefaultErrorHandler를 생성하고 즉시 반환합니다. (지역 변수 제거)
        return new DefaultErrorHandler(recoverer, fixedBackOff);
    }
}