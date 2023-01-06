package tacticalgoat.jlox.core.token;

import tacticalgoat.jlox.core.Util;

import java.util.*;

public class Tokenizer {
    private final String source;
    private int startOfToken = 0;
    private int line = 0;
    private int currentIndex = 0;

    private static final Map<String, TokenType> KEYWORD_TYPE_MAP = new HashMap<>();

    static {
        KEYWORD_TYPE_MAP.put("and", TokenType.AND);
        KEYWORD_TYPE_MAP.put("class", TokenType.CLASS);
        KEYWORD_TYPE_MAP.put("else", TokenType.ELSE);
        KEYWORD_TYPE_MAP.put("false", TokenType.FALSE);
        KEYWORD_TYPE_MAP.put("for", TokenType.FOR);
        KEYWORD_TYPE_MAP.put("fun", TokenType.FUN);
        KEYWORD_TYPE_MAP.put("if", TokenType.IF);
        KEYWORD_TYPE_MAP.put("nil", TokenType.NIL);
        KEYWORD_TYPE_MAP.put("or", TokenType.OR);
        KEYWORD_TYPE_MAP.put("print", TokenType.PRINT);
        KEYWORD_TYPE_MAP.put("return", TokenType.RETURN);
        KEYWORD_TYPE_MAP.put("super", TokenType.SUPER);
        KEYWORD_TYPE_MAP.put("this", TokenType.THIS);
        KEYWORD_TYPE_MAP.put("true", TokenType.TRUE);
        KEYWORD_TYPE_MAP.put("var", TokenType.VAR);
        KEYWORD_TYPE_MAP.put("while", TokenType.WHILE);
    }
    private boolean hadError = false;

    private List<Token> tokens = new ArrayList<>();

    public Tokenizer(final String source) {
        this.source = source;
    }

    private void resetTokenizer() {
        line = 0;
        currentIndex = 0;
        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        resetTokenizer();

        while (!isAtEnd()) {
            startOfToken = currentIndex;
            scanToken();
        }

        return tokens;
    }

    private void scanToken() {
        switch (consumeAndAdvance()) {
            // Single character lexemes
            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);
            case '{' -> addToken(TokenType.LEFT_BRACE);
            case '}' -> addToken(TokenType.RIGHT_BRACE);
            case ',' -> addToken(TokenType.COMMA);
            case '.' -> addToken(TokenType.DOT);
            case '-' -> addToken(TokenType.MINUS);
            case '+' -> addToken(TokenType.PLUS);
            case ';' -> addToken(TokenType.SEMI_COLON);
            case '*' -> addToken(TokenType.STAR);
            // Multi character lexemes
            case '!' -> addToken(matchAndAdvance('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '=' -> addToken(matchAndAdvance('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '<' -> addToken(matchAndAdvance('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>' -> addToken(matchAndAdvance('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);

            // Unknown length lexemes
            case '/' -> {
                if (matchAndAdvance('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        consumeAndAdvance();
                    }
                } else if (matchAndAdvance('*')){
                    parseBlockComment();
                } else {
                    addToken(TokenType.SLASH);
                }
            }

            case ' ', '\r', '\t' -> {}

            case '\n' -> line++;

            case '"' -> parseString();

            // Unrecognized character
            default -> {
                if (isDigit(peek())) {
                    parseNumber();
                } else if (isAlpha(peek())) {
                    parseIdentifier();
                } else {
                    hadError = true;
                    System.out.println(Util.error(line, "Unexpected character"));
                }
            }
        }
    }



    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(currentIndex);
    }

    private char consumeAndAdvance() {
        return source.charAt(currentIndex++);
    }

    private boolean matchAndAdvance(final char expected) {
        if (isAtEnd() || (source.charAt(currentIndex) != expected)) {
            return false;
        }
        currentIndex++;
        return true;
    }

    private void addToken(final TokenType type) {
        addToken(type, null);
    }

    private void addToken(final TokenType type, final Object literal) {
        final String text = source.substring(startOfToken, currentIndex);
        tokens.add(Token.builder()
                .tokenType(type)
                .lexeme(text)
                .literal(Optional.ofNullable(literal))
                .line(line)
                .build());
    }

    private boolean isAtEnd() {
        return currentIndex >= source.length();
    }

    private boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(final char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || (c == '_');
    }

    private boolean isAlphaNumeric(final char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void parseString() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            consumeAndAdvance();
        }

        if (isAtEnd()) {
            System.out.println(Util.error(line, "Unterminated String"));
            return;
        }

        consumeAndAdvance();

        addToken(TokenType.STRING, source.substring(startOfToken + 1, currentIndex - 1));
    }

    private void parseNumber() {
        while (isDigit(peek())) {
            consumeAndAdvance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            consumeAndAdvance();

            while (isDigit(peek())) {
                consumeAndAdvance();
            }
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(startOfToken, currentIndex)));
    }

    private void parseBlockComment() {
        while (!isAtEnd()) {
            final char c = consumeAndAdvance();
            if (c == '/' && peek() == '*') {
                consumeAndAdvance();
                parseBlockComment();
            } else if (c == '*' && peek() == '/') {
                consumeAndAdvance();
                return;
            } else if (c == '\n') {
                line++;
            }
        }
    }

    private void parseIdentifier() {
        while (isAlphaNumeric(peek())) {
            consumeAndAdvance();
        }
        String text = source.substring(startOfToken, currentIndex);
        addToken(KEYWORD_TYPE_MAP.getOrDefault(text, TokenType.IDENTIFIER));
    }

    private char peekNext() {
        if (currentIndex + 1 > source.length()) return '\0';
        return source.charAt(currentIndex + 1);
    }
}
