package com.github.edurbs.makepub2.app.domain;

import jakarta.annotation.Nullable;

public record ScriptureAddress (@Nullable Book book, int chapter, int verse, int endVerse) {
}
