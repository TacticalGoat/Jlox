package tacticalgoat.jlox.core.token;

import lombok.Builder;

import java.util.Optional;

@Builder
public record Token(TokenType tokenType, String lexeme, Optional<Object> literal, int line) {
    public String toString() {
        return tokenType + " " + lexeme + " " + literal;
    }
}
