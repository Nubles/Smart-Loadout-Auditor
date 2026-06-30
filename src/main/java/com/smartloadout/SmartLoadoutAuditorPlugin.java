package com.smartloadout;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@PluginDescriptor(
	name = "Smart Loadout Auditor",
	description = "Audits loadouts against flexible activity templates.",
	tags = {"inventory", "equipment", "loadout", "spellbook", "wilderness"}
)
public class SmartLoadoutAuditorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private Notifier notifier;

	@Inject
	private SmartLoadoutAuditorConfig config;

	private SmartLoadoutAuditorPanel panel;
	private NavigationButton navigationButton;
	private LoadoutStateReader stateReader;
	private final LoadoutAuditEngine auditEngine = new LoadoutAuditEngine();
	private List<ActivityTemplate> templates;
	private boolean bankOpen;

	@Provides
	SmartLoadoutAuditorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SmartLoadoutAuditorConfig.class);
	}

	@Override
	protected void startUp()
	{
		templates = StarterTemplates.templates();
		stateReader = new LoadoutStateReader(client);
		panel = new SmartLoadoutAuditorPanel(index -> runAudit(false), () -> runAudit(false), this::importFromClipboard, this::exportToClipboard);
		panel.setTemplates(templates);

		navigationButton = NavigationButton.builder()
			.tooltip("Smart Loadout Auditor")
			.icon(createIcon())
			.priority(7)
			.panel(panel)
			.build();
		clientToolbar.addNavigation(navigationButton);

		bankOpen = isBankOpen();
		runAudit(false);
	}

	@Override
	protected void shutDown()
	{
		if (navigationButton != null)
		{
			clientToolbar.removeNavigation(navigationButton);
		}

		panel = null;
		navigationButton = null;
		stateReader = null;
		templates = null;
		bankOpen = false;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		boolean currentlyBankOpen = isBankOpen();
		if (bankOpen && !currentlyBankOpen && config.auditOnBankClose())
		{
			runAudit(true);
		}
		bankOpen = currentlyBankOpen;
	}

	private void runAudit(boolean notify)
	{
		if (panel == null || stateReader == null)
		{
			return;
		}

		ActivityTemplate template = panel.selectedTemplate();
		if (template == null && templates != null && !templates.isEmpty())
		{
			template = templates.get(0);
		}
		if (template == null)
		{
			return;
		}

		List<AuditResult> results = auditEngine.audit(template, stateReader.read());
		panel.showResults(results);
		if (notify && config.notifyOnFailures() && hasFailure(results))
		{
			notifier.notify("Smart Loadout Auditor found unresolved loadout issues.");
		}
	}

	private void importFromClipboard()
	{
		try
		{
			String json = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			List<ActivityTemplate> imported = TemplateStore.importTemplates(json);
			if (imported.isEmpty())
			{
				notifier.notify("Smart Loadout Auditor found no valid templates on the clipboard.");
				return;
			}

			templates = imported;
			panel.setTemplates(templates);
			runAudit(false);
			notifier.notify("Smart Loadout Auditor templates imported from clipboard.");
		}
		catch (UnsupportedFlavorException | IOException | IllegalStateException ex)
		{
			notifier.notify("Smart Loadout Auditor could not import templates from clipboard.");
		}
	}

	private void exportToClipboard()
	{
		try
		{
			String json = TemplateStore.exportTemplates(templates);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(json), null);
			notifier.notify("Smart Loadout Auditor templates copied to clipboard.");
		}
		catch (IllegalStateException ex)
		{
			notifier.notify("Smart Loadout Auditor could not copy templates to clipboard.");
		}
	}

	private boolean hasFailure(List<AuditResult> results)
	{
		for (AuditResult result : results)
		{
			if (result.getSeverity() == AuditSeverity.FAIL)
			{
				return true;
			}
		}
		return false;
	}

	private boolean isBankOpen()
	{
		Widget bank = client.getWidget(InterfaceID.BANK, 1);
		return bank != null && !bank.isHidden();
	}

	private static BufferedImage createIcon()
	{
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(29, 33, 38));
		graphics.fillRoundRect(1, 1, 14, 14, 4, 4);
		graphics.setColor(new Color(112, 200, 255));
		graphics.drawRoundRect(1, 1, 13, 13, 4, 4);
		graphics.drawLine(4, 5, 7, 8);
		graphics.drawLine(7, 8, 12, 3);
		graphics.drawLine(4, 11, 12, 11);
		graphics.dispose();
		return image;
	}
}
