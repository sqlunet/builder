package org.sqlbuilder.fn;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.sqlbuilder.AllModules;
import org.sqlbuilder.Builder;
import org.sqlbuilder.Module;
import org.sqlbuilder.Normalizer;
import org.sqlbuilder.Progress;
import org.sqlbuilder.Resources;
import org.sqlbuilder.SQLCommand;
import org.sqlbuilder.wordnet.db.DBWordFinder;

public class FnModule implements Module
{
	public static final String MODULE_ID = "fn";

	protected static final String[] fnTableKeys = Resources.concat("Fn_annosets", "Fn_coretypes", "Fn_corpuses", "Fn_cxns", "Fn_documents", "Fn_fegrouprealizations", "Fn_ferealizations", "Fn_fegrouprealizations_fes", "Fn_fes", "Fn_fes_excluded",
			"Fn_fes_required", "Fn_fes_semtypes", "Fn_fetypes", "Fn_framelexunits", "Fn_framerelations", "Fn_frames", "Fn_frames_related", "Fn_frames_semtypes", "Fn_gftypes", "Fn_governors", "Fn_governors_annosets", "Fn_labels", "Fn_labeltypes",
			"Fn_labelitypes", "Fn_layers", "Fn_layertypes", "Fn_lexemes", "Fn_lexunits", "Fn_lexunits_governors", "Fn_lexunits_semtypes", "Fn_patterns", "Fn_patterns_annosets", "Fn_patterns_valenceunits", "Fn_poses", "Fn_pttypes", "Fn_semtypes",
			"Fn_semtypes_supers", "Fn_sentences", "Fn_sentences_annosets", "Fn_statuses", "Fn_subcorpuses", "Fn_subcorpuses_sentences", "Fn_valenceunits", "Fn_valenceunits_annosets", "Fn_words");

	protected static final String[] fnPresetTableKeys = Resources.concat("Fn_coretypes", "Fn_poses", "Fn_labelitypes");

	protected static final String[] fnFramesTableKeys = Resources.concat("Fn_fetypes", "Fn_fes", "Fn_frames_corefes", "Fn_fes_excluded", "Fn_fes_required", "Fn_fes_semtypes", "Fn_framelexunits", "Fn_frames", "Fn_frames_related", "Fn_frames_semtypes");

	protected static final String[] fnLexicalUnitsTableKeys = Resources.concat("Fn_ferealizations", "Fn_fegrouprealizations", "Fn_framelexunits", "Fn_governors", "Fn_governors_annosets", "Fn_lexemes", "Fn_lexunits", "Fn_lexunits_semtypes", "Fn_patterns",
			"Fn_patterns_annosets", "Fn_patterns_valenceunits", "Fn_fegrouprealizations_fes", "Fn_subcorpuses", "Fn_subcorpuses_sentences", "Fn_valenceunits", "Fn_valenceunits_annosets");

	protected static final String[] fnFullTextTableKeys = Resources.concat("Fn_annosets", "Fn_corpuses", "Fn_documents", "Fn_labels", "Fn_layers", "Fn_sentences", "Fn_sentences_annosets");

	public enum Op
	{
		FN, CREATEFNTABLES, DROPFNTABLES, BUILDFN, BUILDFNPRESET, BUILDFNFRAMES, BUILDFNLUS, BUILDFNFULLTEXT, FLUSHFN, FLUSHFNLOGS, CREATEFNCONSTRAINTS, DROPFNCONSTRAINTS, DUMPCONSTRAINTSFN, DUMPDROPCONSTRAINTSFN, DUMPTABLECREATESFN, NORMALIZEFN, DUMPNORMALIZEFN
	}

	private Properties props = null;

	@Override
	public void setup(final Properties properties)
	{
		this.props = properties;
	}

	@Override
	public void init()
	{
		final boolean addToWn = this.props.getProperty("fnaddwn", "").compareToIgnoreCase("true") == 0;
		DBWordFinder.setup(addToWn, 5000000L, "fn-word");
	}

	@Override
	public String getName()
	{
		return FnModule.MODULE_ID;
	}

	@Override
	public String[] getTableKeys()
	{
		return FnModule.fnTableKeys;
	}

	@Override
	public String[] getXTableKeys()
	{
		return new String[0];
	}

	@Override
	public Object parseCommand(final String string)
	{
		if (string.matches("^fn"))
			return Op.FN;
		if (string.matches("^create fn tables"))
			return Op.CREATEFNTABLES;
		if (string.matches("^drop fn tables"))
			return Op.DROPFNTABLES;
		if (string.matches("^flush fn"))
			return Op.FLUSHFN;
		if (string.matches("^flush fn logs"))
			return Op.FLUSHFNLOGS;
		if (string.matches("^build fn"))
			return Op.BUILDFN;
		if (string.matches("^build fn presets"))
			return Op.BUILDFNPRESET;
		if (string.matches("^build fn frames"))
			return Op.BUILDFNFRAMES;
		if (string.matches("^build fn lus"))
			return Op.BUILDFNLUS;
		if (string.matches("^build fn ft"))
			return Op.BUILDFNFULLTEXT;
		if (string.matches("^create fn constraints"))
			return Op.CREATEFNCONSTRAINTS;
		if (string.matches("^drop fn constraints"))
			return Op.DROPFNCONSTRAINTS;
		if (string.matches("^normalize fn"))
			return Op.NORMALIZEFN;
		if (string.matches("^dump table creates fn"))
			return Op.DUMPTABLECREATESFN;
		if (string.matches("^dump constraints fn"))
			return Op.DUMPCONSTRAINTSFN;
		if (string.matches("^dump drop constraints fn"))
			return Op.DUMPDROPCONSTRAINTSFN;
		if (string.matches("^dump normalize fn"))
			return Op.DUMPNORMALIZEFN;
		return null;
	}

	@Override
	public void help()
	{
		System.out.println("FN");
		System.out.println(" fn");
		System.out.println(" create fn tables");
		System.out.println(" drop fn tables");
		System.out.println(" drop fn tables");
		System.out.println(" flush fn");
		System.out.println(" flush fn logs");
		System.out.println(" build fn");
		System.out.println(" build fn presets");
		System.out.println(" build fn frames");
		System.out.println(" build fn lus");
		System.out.println(" build fn ft");
		System.out.println(" normalize fn");
		System.out.println(" dump table creates fn");
		System.out.println(" dump constraints fn");
		System.out.println(" dump drop constraints fn");
		System.out.println(" dump normalize fn");
	}

	@Override
	public boolean build(final Object command, final Properties props) throws SQLException, IOException
	{
		if (!(command instanceof Op))
			return false;
		final Op op = (Op) command;
		switch (op)
		{
			case FN:
				SQLCommand.makeLogs();
				SQLCommand.flushLogs(FnModule.MODULE_ID);
				build(Op.DROPFNCONSTRAINTS, props);
				// Builder.sleep();
				build(Op.DROPFNTABLES, props);
				// Builder.sleep();
				build(Op.CREATEFNTABLES, props);
				build(Op.FLUSHFN, props);
				build(Op.BUILDFN, props);
				build(Op.NORMALIZEFN, props);
				build(Op.CREATEFNCONSTRAINTS, props);
				return true;

			case BUILDFN:
				FnModule.build(props);
				return true;

			case BUILDFNPRESET:
				SQLCommand.flush(FnModule.fnPresetTableKeys);
				FnModule.buildPresets();
				return true;

			case BUILDFNFRAMES:
				SQLCommand.flush(FnModule.fnFramesTableKeys);
				FnModule.buildFrames(props);
				return true;

			case BUILDFNLUS:
				SQLCommand.flush(FnModule.fnLexicalUnitsTableKeys);
				FnModule.buildLexicalUnits(props);
				return true;

			case BUILDFNFULLTEXT:
				SQLCommand.flush(FnModule.fnFullTextTableKeys);
				FnModule.buildFullText(props);
				return true;

			case CREATEFNTABLES:
				SQLCommand.create(FnModule.fnTableKeys);
				return true;

			case DROPFNTABLES:
				SQLCommand.drop(FnModule.fnTableKeys);
				return true;

			case FLUSHFN:
				SQLCommand.flush(FnModule.fnTableKeys);
				return true;

			case CREATEFNCONSTRAINTS:
				SQLCommand.createConstraints(FnModule.fnTableKeys);
				return true;

			case DROPFNCONSTRAINTS:
				SQLCommand.dropConstraints(FnModule.fnTableKeys);
				return true;

			case FLUSHFNLOGS:
				SQLCommand.flushLogs(FnModule.MODULE_ID);
				break;

			case NORMALIZEFN:
				Progress.traceHeader("indexes", "starting");
				SQLCommand.createAllIndex(FnModule.fnTableKeys);
				Progress.traceTailer("indexes", "done", FnModule.fnTableKeys.length);
				Progress.traceHeader("normalize", "starting");
				FnModule.normalize(true);
				Progress.traceTailer("normalize", "done", 13);
				break;

			case DUMPTABLECREATESFN:
				AllModules.dumpTableCreates(FnModule.fnTableKeys);
				return true;

			case DUMPCONSTRAINTSFN:
				AllModules.dumpConstraints(false, FnModule.fnTableKeys);
				return true;

			case DUMPDROPCONSTRAINTSFN:
				AllModules.dumpDropConstraints(FnModule.fnTableKeys);
				return true;

			case DUMPNORMALIZEFN:
				FnModule.normalize(false);
				break;

			default:
				break;
		}

		return false;
	}

	@Override
	public ResourceBundle getResource(final Module.DBType type)
	{
		final String resourceName = getClass().getPackage().getName() + '.' + type.name().toLowerCase();
		return ResourceBundle.getBundle(resourceName);
	}

	private static void build(final Properties props) throws IOException
	{
		new FnPresetProcessor().process();
		new FnSemTypeProcessor("semTypes.xml", props).process();
		new FnLexUnitProcessor(props).process();
		new FnFrameProcessor(props).process();
		new FnFullTextProcessor(props).process();
	}

	private static void buildPresets() throws IOException
	{
		new FnPresetProcessor().process();
	}

	private static void buildFrames(final Properties props) throws IOException
	{
		new FnFrameProcessor(props).process();
	}

	private static void buildLexicalUnits(final Properties props) throws IOException
	{
		new FnLexUnitProcessor(props).process();
	}

	private static void buildFullText(final Properties props) throws IOException
	{
		new FnFullTextProcessor(props).process();
	}

	private static void normalize(final boolean execute) throws SQLException
	{
		System.out.println("\n-- normalize frame reference");
		new Normalizer("Fn_frames", "frame", "frameid") //
				.targets("Fn_frames_related", "frame2", "frame2id") //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize frame relation reference");
		new Normalizer("Fn_framerelations", "relation", "relationid") //
				.targets("Fn_frames_related", "relation", "relationid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.newPk("frameid,frame2id,relationid").exec(execute);

		System.out.println("\n-- normalize fetypes reference");
		new Normalizer("Fn_fetypes", "fetype", "fetypeid") //
				.targets("Fn_fes", "fetype", "fetypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.targets("Fn_ferealizations", "fetype", "fetypeid") //
				.reference() //
				.cleanup() //
				.targets("Fn_lexunits", "incorporatedfetype", "incorporatedfetypeid") //
				.reference() //
				.cleanup() //
				.targets("Fn_patterns_valenceunits", "fetype", "fetypeid") //
				.reference() //
				.cleanup() //
				.targets("Fn_fegrouprealizations_fes", "fetype", "fetypeid") //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize pt reference");
		new Normalizer("Fn_pttypes", "pt", "ptid") //
				.targets("Fn_valenceunits", "pt", "ptid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize gf reference");
		new Normalizer("Fn_gftypes", "gf", "gfid") //
				.targets("Fn_valenceunits", "gf", "gfid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize layertype reference");
		new Normalizer("Fn_layertypes", "layertype", "layertypeid") //
				.targets("Fn_layers", "layertype", "layertypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize labeltype reference");
		new Normalizer("Fn_labeltypes", "labeltype", "labeltypeid") //
				.targets("Fn_labels", "labeltype", "labeltypeid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize status reference");
		new Normalizer("Fn_statuses", "status", "statusid") //
				.targets("Fn_lexunits", "status", "statusid") //
				.create() //
				.insert() //
				.reference() //
				.cleanup() //
				.targets("Fn_annosets", "status", "statusid") //
				.insert() //
				.reference() //
				.cleanup() //
				.exec(execute);

		System.out.println("\n-- normalize cxn reference");
		new Normalizer("Fn_cxns", "cxn", "cxnid") //
				.targets("Fn_annosets", "cxn", "cxnid") //
				.create2() //
				.insert2() //
				.cleanup() //
				.exec(execute);

		// infer feid from fetypeid and other data from joined tables

		System.out.println("\n-- normalize ferealizations (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_ferealizations", "fetypeid", "feid") //
				.referenceThrough("Fn_lexunits", "luid", "Fn_frames", "frameid") //
				.exec(execute);

		System.out.println("\n-- normalize fegrouprealizations (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_fegrouprealizations_fes", "fetypeid", "feid") //
				.referenceThrough("Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid", "Fn_frames", "frameid") //
				.exec(execute);

		System.out.println("\n-- normalize lexunits (incorporatedfeid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_lexunits", "incorporatedfetypeid", "incorporatedfeid") //
				.referenceThrough("Fn_frames", "frameid") //
				.exec(execute);

		System.out.println("\n-- normalize patterns_valenceunits (feid)");
		new Normalizer("Fn_fes", "fetypeid", "feid") //
				.targets("Fn_patterns_valenceunits", "fetypeid", "feid") //
				.referenceThrough("Fn_patterns", "patternid", "Fn_fegrouprealizations", "fegrid", "Fn_lexunits", "luid") //
				.exec(execute);
	}

	public static void main(final String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException
	{
		final Properties props = Builder.standalone(args);

		// setup
		final Module module = new FnModule();
		Builder.setUpModule(module, props);

		final String command = args[1];
		module.build(module.parseCommand(command), props);
		System.err.println("\nframenet done");
	}
}
