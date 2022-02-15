package org.sqlbuilder.vn.collector;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.common.XPathUtils;
import org.sqlbuilder.vn.Inherit;
import org.sqlbuilder.vn.VnDocument;
import org.sqlbuilder.vn.VnModule;
import org.sqlbuilder.vn.joins.*;
import org.sqlbuilder.vn.objects.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class VnProcessor extends Processor
{
	protected final String verbNetHome;

	public VnProcessor(final Properties props)
	{
		super("vn");
		this.verbNetHome = props.getProperty("vnhome", System.getenv().get("VNHOME"));
	}

	@Override
	public void run()
	{
		final File folder = new File(this.verbNetHome);
		final FilenameFilter filter = (dir, name) -> name.endsWith(".xml");

		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
		{
			return;
		}
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));

		// iterate

		int fileCount = 0;
		Progress.traceHeader("reading verbnet files", "");
		for (final File file : files)
		{
			fileCount++;
			processVerbNetFile(file.getAbsolutePath(), file.getName());
		}
		Progress.traceTailer(fileCount);
	}

	protected void processVerbNetFile(final String fileName, final String name)
	{
		final String head = name.split("-")[0];
		Progress.tracePending("verbnet",head);
		try
		{
			final VnDocument document = new VnDocument(fileName);
			processVerbNetClass(XPathUtils.getXPath(document.getDocument(), "./VNCLASS"), head, null, null);
			Progress.traceDone(null);
		}
		catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
			Progress.traceDone(e.toString());
		}
	}

	protected void processVerbNetClass(final Node start, final String head, final Collection<Role> inheritedRoles, final Collection<Frame> inheritedFrames)
	{
		try
		{
			final VnClass clazz = processClass(start);
			processItems(start);
			processMembers(start, head, clazz);
			Collection<Role> inheritableRoles = processRoles(start, clazz, inheritedRoles);
			Collection<Frame> inheritableFrames = processFrames(start, clazz, inheritedFrames);

			// recurse
			final NodeList subclasses = XPathUtils.getXPaths(start, "./SUBCLASSES/VNSUBCLASS");
			for (int i = 0; i < subclasses.getLength(); i++)
			{
				final Node subNode = subclasses.item(i);
				processVerbNetClass(subNode, head, inheritableRoles, inheritableFrames);
			}
		}
		catch (XPathExpressionException | TransformerException | ParserConfigurationException | SAXException | IOException e)
		{
			Logger.instance.logXmlException(VnModule.MODULE_ID, this.tag, "read-class", head, -1, null, "xml", e);
		}
	}

	private static VnClass processClass(final Node start) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		return VnDocument.makeClass(start);
	}

	private static void processItems(final Node start) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// get groupings
		VnDocument.makeGroupings(start);

		// get selection restrs
		VnDocument.makeSelRestrs(start);

		// get syntactic restrs
		VnDocument.makeSynRestrs(start);

		// get selection restr types
		VnDocument.makeSelRestrTypes(start);

		// get syntactic restr types
		VnDocument.makeSynRestrTypes(start);

		// get frame names
		VnDocument.makeFrameNames(start);

		// get frame subnames
		VnDocument.makeFrameSubNames(start);

		// get frame examples
		VnDocument.makeFrameExamples(start);

		// get frame example mappings
		VnDocument.makeFrameExampleMappings(start);

		// get predicates
		VnDocument.makePredicates(start);

		// get predicate semantics mappings
		VnDocument.makePredicateSemanticsMappings(start);

		// get syntaxes
		VnDocument.makeSyntaxes(start);

		// get semantics
		VnDocument.makeSemantics(start);

		// get role types
		VnDocument.makeRoleTypes(start);

		// get roles
		VnDocument.makeRoles(start);

		// get frames
		VnDocument.makeFrames(start);

	}

	private static Collection<Role> processRoles(final Node start, final VnClass clazz, final Collection<Role> inheritedRoles) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// roles
		Collection<Role> roles = VnDocument.makeRoles(start);
		if (inheritedRoles != null)
		{
			roles = Inherit.mergeRoles(roles, inheritedRoles);
		}

		// collect roles
		for (Role role : roles)
		{
			Class_Role.make(clazz, role);
		}

		// return data to be inherited by subclasses
		return roles;
	}

	private static Collection<Frame> processFrames(final Node start, final VnClass clazz, final Collection<Frame> inheritedFrames) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException
	{
		// roles
		Collection<Frame> frames = VnDocument.makeFrames(start);
		if (inheritedFrames != null)
		{
			frames = Inherit.mergeFrames(frames, inheritedFrames);
		}

		// collect frames
		for (Frame frame : frames)
		{
			Class_Frame.make(clazz, frame);
		}

		// return data to be inherited by subclasses
		return frames;
	}

	private static void processMembers(final Node start, final String head, final VnClass clazz) throws XPathExpressionException
	{
		// members
		Collection<Member> members = VnDocument.makeMembers(start);
		members.add(Member.make(head, null, null));

		// member
		for (final Member member : members)
		{
			// word
			final Word word = Word.make(member.lemma);

			// groupings
			if (member.groupings != null)
			{
				for (final Grouping grouping : member.groupings)
				{
					Member_Grouping.make(clazz, word, grouping);
				}
			}

			// membership
			final Class_Word membership = Class_Word.make(clazz, word);

			// if sensekeys are null, data apply to all senses
			if (member.senseKeys == null)
			{
				// class member sense
				Member_Sense.make(membership, 0, null, 1.F);
				continue;
			}

			// else if sensekeys are not null, data apply only to senses pointed at by sensekeys
			int i = 1;
			for (final Sensekey sensekey : member.senseKeys)
			{
				// sense mapping quality as indicated by verbnet ('?' prefix to sense key)
				final float senseQuality = sensekey.getQuality();

				// class member sense
				Member_Sense.make(membership, i, sensekey, senseQuality);

				i++;
			}
		}
	}
}
