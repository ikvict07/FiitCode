package org.fiit.fiitcode.RunCode.executor.Enums;

public enum SupportedLanguages {
    Python,
    Cpp;

    public String getExtension() {
        return switch (this) {
            case Python -> "py";
            case Cpp -> "cpp";
        };
    }
}
