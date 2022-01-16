package org.sqlbuilder.fn;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.Insertable;
import org.sqlbuilder.Logger;
import org.sqlbuilder.NotFoundException;
import org.sqlbuilder.Progress;
import org.sqlbuilder.SQLUpdateException;

import edu.berkeley.icsi.framenet.SemTypeType;
import edu.berkeley.icsi.framenet.SemTypeType.SuperType;
import edu.berkeley.icsi.framenet.SemTypesDocument;

public class FnSemTypeProcessor extends FnProcessor1
{
	public FnSemTypeProcessor(final String filename, final Properties props)
	{
		super(filename, props);
		this.processorTag = "semtype";
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name, final Connection connection)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (semtype)", name);
		}
		final int count = 0;
		final File xmlFile = new File(fileName);
		// System.out.printf("file=<%s>\n", xmlFile);
		try
		{
			final SemTypesDocument document = SemTypesDocument.Factory.parse(xmlFile);

			final SemTypeType[] semtypes = document.getSemTypes().getSemTypeArray();
			for (final SemTypeType semtype : semtypes)
			{
				final long semtypeid = semtype.getID();

				final FnSemType fnSemType = new FnSemType(semtype);
				// System.out.println(fnSemType);
				insert(fnSemType, connection);

				final SuperType[] supertypes = semtype.getSuperTypeArray();
				for (final SuperType supertype : supertypes)
				{
					final long superid = supertype.getSupID();
					final FnSemTypeSuper fnSemSuperType = new FnSemTypeSuper(semtypeid, superid);
					insert(fnSemSuperType, connection);
				}
			}
		}
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.processorTag, "xml-document", fileName, -1L, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (semtype)", name, count);
		}
		return 1;
	}

	// convenience function

	void insert(final Insertable insertable, final Connection connection)
	{
		try
		{
			insertable.insert(connection);
		}
		catch (NotFoundException nfe)
		{
			Logger.instance.logNotFoundException(FnModule.MODULE_ID, this.processorTag, "find-" + insertable.getClass().getName().toLowerCase(), this.filename, -1L, null, insertable.toString(), nfe);
		}
		catch (SQLUpdateException sqlue)
		{
			Logger.instance.logSQLUpdateException(FnModule.MODULE_ID, this.processorTag, "insert-" + insertable.getClass().getName().toLowerCase(), this.filename, -1L, null, insertable.toString(), sqlue);
		}
	}
}
