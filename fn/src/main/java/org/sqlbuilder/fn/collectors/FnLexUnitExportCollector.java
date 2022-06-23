package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.AlreadyFoundException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.joins.*;
import org.sqlbuilder.fn.objects.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.berkeley.icsi.framenet.LexUnitDocument;
import edu.berkeley.icsi.framenet.ValenceUnitType;
import edu.berkeley.icsi.framenet.ValencesType;

public class FnLexUnitExportCollector extends FnCollector
{
	private final boolean skipLayers;

	public FnLexUnitExportCollector(final Properties props)
	{
		super("lu", props, "lu");
		this.skipLayers = props.getProperty("fn_skip_layers", "").compareToIgnoreCase("true") == 0;
	}

	private final Map<ValenceUnit, FERealization> vuToFer = new HashMap<>();

	@Override
	protected void processFrameNetFile(final String fileName)
	{
		vuToFer.clear();

		final File file = new File(fileName);
		try
		{
			final LexUnitDocument _document = LexUnitDocument.Factory.parse(file);

			// L E X U N I T

			final LexUnitDocument.LexUnit _lexunit = _document.getLexUnit();
			final int luid = _lexunit.getID();
			final int frameid = _lexunit.getFrameID();
			LexUnit.make(_lexunit);
		}
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e);
		}
	}
}
