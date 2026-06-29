package com.smartloadout;

public class AuditResult
{
    private final AuditSeverity severity;
    private final String title;
    private final String message;

    public AuditResult(AuditSeverity severity, String title, String message)
    {
        this.severity = severity;
        this.title = title;
        this.message = message;
    }

    public AuditSeverity getSeverity()
    {
        return severity;
    }

    public String getTitle()
    {
        return title;
    }

    public String getMessage()
    {
        return message;
    }
}
