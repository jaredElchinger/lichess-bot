package org.jem.lichess.lichessbot.chess;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@Slf4j
@RequiredArgsConstructor
public class StockFish {

    private final String path;

    private Process engineProcess;
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    /**
     * Starts Stockfish engine as a process and initializes it
     *
     * @return True on success. False otherwise
     */
    public boolean startEngine() {
        try {
            engineProcess = Runtime.getRuntime().exec(path);
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
        } catch (Exception e) {
            log.error("Error starting engine, ", e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Takes in any valid UCI command and executes it
     *
     * @param command
     */
    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is generally called right after 'sendCommand' for getting the raw
     * output from Stockfish
     *
     * @return Raw output from Stockfish
     */
    public String getOutput(final String waitForString) {
        StringBuffer buffer = new StringBuffer();
        try {
            boolean run = true;
            while (run) {
                String text = processReader.readLine();
                if (text.contains(waitForString)) {
                    buffer.append(text).append("\n");
                    run = false;
                } else {
                    buffer.append(text).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * This function returns the best move for a given position after
     * calculating for 'waitTime' ms
     *
     * @param moves    Position string
     * @param waitTime in milliseconds
     * @return Best Move in PGN format
     */
    @SneakyThrows
    public String getBestMove(long waitTime, String moves, long wtime, long btime, long winc, long binc) {
        sendCommand(String.format("position startpos moves %s", moves));

        String getMoveCommand = String.format("go wtime %s btime %s winc %s binc %s", wtime, btime, winc, binc);
        if (waitTime > 0 || moves.split(" ").length <= 1) {
            getMoveCommand += String.format(" movetime %s", waitTime);
        } else if (moves.split(" ").length <= 1) {
            getMoveCommand += String.format(" movetime %s", "20000"); //For now hard code - so starts don't think so long
        }
        sendCommand(getMoveCommand);

        final String output = getOutput("bestmove");
        final String[] bestMovesResponse = output.split("bestmove ");
        if (bestMovesResponse.length > 1) {
            return bestMovesResponse[1].split(" ")[0];
        } else {
            log.error("Attempted uci commands of 'position startpos moves {}' & 'go movetime {} wtime {} btime {} winc {} binc {}', Output from stock fish, \n{}",
                    moves, waitTime, wtime, btime, winc, binc, output);
            throw new IllegalStateException("Unable to parse out best moves from Stockfish output");
        }
    }

    /**
     * Stops Stockfish and cleans up before closing it
     */
    public void stopEngine() {
        try {
            sendCommand("quit");
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
        }
    }

}
