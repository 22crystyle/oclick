package org.oclick.shell;

import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class NameCompletionProvider implements ValueProvider {

    private static final List<String> NAMES = Arrays.asList("World", "User", "Gemini");

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        String input = completionContext.currentWord() != null ? completionContext.currentWord() : "";
        return NAMES.stream()
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .map(CompletionProposal::new)
                .toList();
    }
}
