package org.sqlbuilder.vn;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
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
	protected void run()
	{
		Progress.traceHeader("verbnet pass4", null);
		super.run();
	}

	@Override
	protected void processVerbNetClass(final VnDocument document, final Node start, final String head)
	{
		processVerbNetClass(document, start, head, null);
	}

	private void processVerbNetClass(final VnDocument document, final Node start, final String head, final VnClassData inheritedData)
	{
		try
		{
			// class
			final String className = VnDocument.getClassName(start);
			final VnClass vnclass = new VnClass(className);

			// get class data and merge with inherited data
			final VnClassData data = VnDocument.getClassData(start, vnclass);
			if (inheritedData != null)
			{
				data.merge(inheritedData);
			}

			// members
			Collection<VnItem> words = VnDocument.getItems(start);
			if (words == null)
			{
				words = new ArrayList<>();
				words.add(VnItem.make(head, null, null));
			}

			// ascribe data to each class member
			for (final VnItem item : words)
			{
				// word
				final VnWord vnWord = new VnWord(item.lemma);
				VnWord.SET.add(vnWord);

				// class member
				final VnMember member = new VnMember(vnclass, vnWord);
				VnMember.SET.add(member);

				// if sensekeys are null, data apply to all senses
				if (item.senseKeys == null)
				{
					// class member sense
					final VnMemberSense memberSense = new VnMemberSense(member, 0, null, 1.F);
					VnMemberSense.SET.add(memberSense);

					// quality = sense mapping quality as indicated by verbnet ('?' prefix to sense key)
					final VnClassDataMapping mapping = new VnClassDataMapping(data);
					VnClassDataMapping.SET.add(mapping);

					// trace
					if (Progress.hyperverbose)
					{
						Progress.trace("vnclass member>", "word=[" + vnWord + "] lemma=[" + item.lemma + "]");
					}
					continue;
				}

				// else if sensekeys are not null, data apply only to senses pointed at by sensekeys
				int i = 1;
				for (final VnSensekey sensekey : item.senseKeys)
				{
					// sense mapping quality as indicated by verbnet ('?' prefix to sense key)
					final float senseQuality = sensekey.getQuality();

					// class member sense
					final VnMemberSense memberSense = new VnMemberSense(member, i, sensekey, senseQuality);
					VnMemberSense.SET.add(memberSense);

					// collect roles and frames
					final VnClassDataMapping mapping = new VnClassDataMapping(data);
					VnClassDataMapping.SET.add(mapping);

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
					for (final VnGrouping grouping : item.groupings)
					{
						final VnGroupingMapping vnGroupingMapping = new VnGroupingMapping(vnWord, vnclass, grouping);

						// collect
						VnGroupingMapping.SET.add(vnGroupingMapping);

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
