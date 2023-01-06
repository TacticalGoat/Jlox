package core.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tacticalgoat.jlox.core.token.Token;
import tacticalgoat.jlox.core.token.TokenType;
import tacticalgoat.jlox.core.token.Tokenizer;

import java.util.List;
import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenizerTest {

    @ParameterizedTest(name = "Test parsing {0} as {1}")
    @MethodSource("provideArgsForBasicParseForIndividualTokens")
    public void when_provided_a_single_token_parse_correctly(final String source, final TokenType expectedOutput) {
        final var tokenizer = new Tokenizer(source);
        final List<Token> tokens = tokenizer.tokenize();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, tokens.size()),
                () -> Assertions.assertEquals(expectedOutput, tokens.get(0).tokenType())
        );
    }

    private static Stream<Arguments> provideArgsForBasicParseForIndividualTokens() {
        return Stream.of(
                Arguments.of("+", TokenType.PLUS),
                Arguments.of("-", TokenType.MINUS),
                Arguments.of("!=", TokenType.BANG_EQUAL),
                Arguments.of("==", TokenType.EQUAL_EQUAL),
                Arguments.of(">=", TokenType.GREATER_EQUAL),
                Arguments.of("<=", TokenType.LESS_EQUAL),
                Arguments.of("\"STRING\"", TokenType.STRING),
                Arguments.of("123", TokenType.NUMBER),
                Arguments.of("123.456", TokenType.NUMBER),
                Arguments.of("random", TokenType.IDENTIFIER),
                Arguments.of("var", TokenType.VAR)
        );
    }
}
