package com.dumbdogdiner.stickyapi.common.util.commonmarkextensions;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCColorFormatBlockParser extends AbstractBlockParser {
    private ChatColor color;


    @Getter
    private Block block;


    private static final Pattern MATCHME = Pattern.compile("\\s*&([0-9a-fk-o])\\s(.*)&&");

    public MCColorFormatBlockParser(ChatColor color) {
        this.color = color;
        this.block = new MCColorFormatBlock(color);
    }

    @Override
    public boolean canContain(Block block) {
        return block != null && !MCColorFormatBlock.class.isAssignableFrom(block.getClass());
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        CharSequence fullLine = state.getLine();
        CharSequence currentLine = fullLine.subSequence(state.getColumn() + state.getIndent(), fullLine.length());

        Matcher matcher = MATCHME.matcher(currentLine);
        if (matcher.matches()) {
            if (color.equals(ChatColor.getByChar(matcher.group(1)))) {
                return BlockContinue.atColumn(state.getColumn() + state.getIndent() + matcher.start(2));
            }
        }

        return BlockContinue.none();
    }

    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            CharSequence fullLine = state.getLine();
            CharSequence line = fullLine.subSequence(state.getColumn(), fullLine.length());
            Matcher matcher = MATCHME.matcher(line);
            if (matcher.matches()) {
                return BlockStart
                        .of(new MCColorFormatBlockParser(ChatColor.getByChar(matcher.group(1))))
                        .atColumn(state.getColumn() + state.getIndent() + matcher.start(2));
            }
            return BlockStart.none();
        }
    }
}
