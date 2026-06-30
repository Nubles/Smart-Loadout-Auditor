package com.smartloadout;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.runelite.client.ui.PluginPanel;

public class SmartLoadoutAuditorPanel extends PluginPanel
{
	private final JComboBox<String> templateSelector = new JComboBox<>();
	private final JTextArea results = new JTextArea();
	private List<ActivityTemplate> templates = Collections.emptyList();

	public SmartLoadoutAuditorPanel(
		Consumer<Integer> onTemplateSelected,
		Runnable onAuditRequested,
		Runnable onImportRequested,
		Runnable onExportRequested)
	{
		setLayout(new BorderLayout(0, 8));

		JPanel top = new JPanel(new GridLayout(0, 1, 0, 4));
		top.add(new JLabel("Activity template"));
		top.add(templateSelector);

		JButton audit = new JButton("Run audit");
		JButton importButton = new JButton("Import JSON");
		JButton exportButton = new JButton("Export JSON");
		top.add(audit);
		top.add(importButton);
		top.add(exportButton);
		add(top, BorderLayout.NORTH);

		results.setEditable(false);
		results.setLineWrap(true);
		results.setWrapStyleWord(true);
		add(new JScrollPane(results), BorderLayout.CENTER);

		templateSelector.addActionListener(event -> onTemplateSelected.accept(templateSelector.getSelectedIndex()));
		audit.addActionListener(event -> onAuditRequested.run());
		importButton.addActionListener(event -> onImportRequested.run());
		exportButton.addActionListener(event -> onExportRequested.run());
	}

	public void setTemplates(List<ActivityTemplate> templates)
	{
		this.templates = templates == null ? Collections.emptyList() : templates;
		templateSelector.removeAllItems();
		for (ActivityTemplate template : this.templates)
		{
			templateSelector.addItem(template.getName());
		}
	}

	public void showResults(List<AuditResult> auditResults)
	{
		StringBuilder builder = new StringBuilder();
		for (AuditResult result : auditResults)
		{
			builder.append(result.getSeverity()).append(": ").append(result.getTitle()).append('\n');
			builder.append(result.getMessage()).append("\n\n");
		}
		results.setText(builder.toString());
		results.setCaretPosition(0);
	}

	public ActivityTemplate selectedTemplate()
	{
		int index = templateSelector.getSelectedIndex();
		if (index < 0 || index >= templates.size())
		{
			return null;
		}
		return templates.get(index);
	}
}
