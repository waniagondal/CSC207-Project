package use_case;

import frameworks_and_drivers.speech_to_text.SpeechRecognizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.speech_to_text.SpeechToTextInputData;
import use_case.speech_to_text.SpeechToTextInteractor;
import use_case.speech_to_text.SpeechToTextOutputBoundary;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpeechToTextInteractorTest {

    private SpeechRecognizer speechRecognizer;
    private SpeechToTextOutputBoundary outputBoundary;
    private SpeechToTextInteractor interactor;
    private StringBuilder outputResult;

    @BeforeEach
    void setUp() {
        // Simple implementation of SpeechRecognizer
        speechRecognizer = audioData -> {
            if (audioData == null || audioData.length == 0) {
                throw new IOException("Invalid audio data");
            }
            return "Recognized text";
        };

        // Simple implementation of SpeechToTextOutputBoundary
        outputResult = new StringBuilder();
        outputBoundary = outputData -> outputResult.append(outputData.getTranscription());

        // Initialize the interactor
        interactor = new SpeechToTextInteractor(speechRecognizer, outputBoundary);
    }

    @Test
    void testProcessSpeech_Success() throws Exception {
        // Arrange
        byte[] audioData = "test audio".getBytes();
        SpeechToTextInputData inputData = new SpeechToTextInputData(audioData);

        // Act
        interactor.processSpeech(inputData);

        // Assert
        assertEquals("Recognized text", outputResult.toString());
    }

    @Test
    void testProcessSpeech_EmptyAudioData() {
        // Arrange
        byte[] audioData = new byte[0];
        SpeechToTextInputData inputData = new SpeechToTextInputData(audioData);

        // Act & Assert
        Exception exception = assertThrows(IOException.class, () -> interactor.processSpeech(inputData));
        assertEquals("Invalid audio data", exception.getMessage());
    }

    @Test
    void testProcessSpeech_NullAudioData() {
        // Arrange
        SpeechToTextInputData inputData = new SpeechToTextInputData(null);

        // Act & Assert
        Exception exception = assertThrows(IOException.class, () -> interactor.processSpeech(inputData));
        assertEquals("Invalid audio data", exception.getMessage());
    }
}