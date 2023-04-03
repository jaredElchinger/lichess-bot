package org.jem.lichess.lichessbot.service.game.subscribers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.core.io.buffer.DataBuffer;

import java.io.InputStream;

@RequiredArgsConstructor
@Slf4j
public class GameStreamSubscriber implements Subscriber<DataBuffer> {

    private final String gameId;

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("Starting subscription of Lichess game {} . . . ", gameId);
    }

    @Override
    public void onNext(DataBuffer dataBuffer) {

        try (final InputStream inputStream = dataBuffer.asInputStream()) {

            final String line = new String(inputStream.readAllBytes()).trim();
            if (!line.isBlank()) {
                log.info(line);
            }

        } catch (Exception e) {
            throw new RuntimeException(String.format("Encountered issue while reading next stream of Lichess game %s, ", this.gameId), e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Encountered an error for Lichess game {}, ", gameId, throwable);
    }

    @Override
    public void onComplete() {
        log.info("Ending subscription of Lichess game {}.", gameId);
        log.info("Subscription of Lichess game {} has ended . . . ", gameId);
    }

}
