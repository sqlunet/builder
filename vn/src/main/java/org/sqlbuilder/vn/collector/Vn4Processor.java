package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.sqlbuilder.vn.joins.*;
import org.sqlbuilder.vn.objects.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class Vn4Processor extends VnProcessor
{
	public Vn4Processor(final Properties props)
	{
		super(props, "vn4");
	}

	@Override
	public void run()
	{
		Progress.traceHeader("verbnet pass4", null);
		super.run();
	}

	@Override
	protected void processVerbNetClass(final VnDocument document, final Node start, final String head)
	{
		processVerbNetClass(document, start, head, null);
	}

	private void processVerbNetClass(final VnDocument document, final Node start, final String head, final Roles_Frames inheritedData)
	{
		try
		{
			// class
			final String className = VnDocument.getClassName(start);
			final VnClass vnclass = VnClass.make(className);

			// get class data and merge with inherited data
			final Roles_Frames data = VnDocument.getClassData(start);
			if (inheritedData != null)
			{
				data.merge(inheritedData);
			}

			// collect roles and frames
			for (Role role : data.getRoles())
			{
				Class_Role.make(vnclass, role);
			}
			for (Frame frame : data.getFrames())
			{
				Class_Frame.make(vnclass, frame);
			}

			// members
			Collection<Member> words = VnDocument.getItems(start);
			if (words == null)
			{
				words = new ArrayList<>();
				words.add(Member.make(head, null, null));
			}

			// ascribe data to each class member
			for (final Member item : words)
			{
				// word
				final VnWord vnWord = VnWord.make(item.lemma);

				// class member
				final Class_Word member = Class_Word.make(vnclass, vnWord);

				// if sensekeys are null, data apply to all senses
				if (item.senseKeys == null)
				{
					// class member sense
					final Member_Sense memberSense = Member_Sense.make(member, 0, null, 1.F);
					Member_Sense.SET.add(memberSense);

					// trace
					if (Progress.hyperverbose)
					{
						Progress.trace("vnclass member>", "word=[" + vnWord + "] lemma=[" + item.lemma + "]");
					}
					continue;
				}

				// else if sensekeys are not null, data apply only to senses pointed at by sensekeys
				int i = 1;
				for (final Sensekey sensekey : item.senseKeys)
				{
					// sense mapping quality as indicated by verbnet ('?' prefix to sense key)
					final float senseQuality = sensekey.getQuality();

					// class member sense
					final Member_Sense memberSense = Member_Sense.make(member, i, sensekey, senseQuality);
					Member_Sense.SET.add(memberSense);

					i++;

					// trace
					if (Progress.hyperverbose)
					{
						Progress.trace("vnclass member>", "word=[" + vnWord + "] sense=[" + sensekey + "]");
					}
				}

				// groupings
				if (item.groupings != null)
				{
					for (final Grouping grouping : item.groupings)
					{
						final Member_Grouping vnGroupingMapping = Member_Grouping.make(vnclass, vnWord, grouping);

						// collect
						Member_Grouping.SET.add(vnGroupingMapping);

						// trace
						if (Progress.hyperverbose)
						{
							Progress.trace("vnclass member grouping>", "word=[" + vnWord + "] groupingmapping=[" + vnGroupingMapping + "] grouping=[" + grouping + "]");
						}
					}
				}
			}

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(document, subNode, head, data);
			}
		}
		catch (TransformerException | XPathExpressionException | ParserConfigurationException | SAXException | IOException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, this.tag, "read-class", document.getFileName(), -1, null, "transformer", e);
		}
	}
}
