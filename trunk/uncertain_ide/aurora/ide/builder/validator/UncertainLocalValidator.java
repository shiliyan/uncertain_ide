package aurora.ide.builder.validator;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import uncertain.composite.CompositeMap;
import aurora.ide.builder.AuroraBuilder;
import aurora.ide.builder.processor.AbstractProcessor;
import aurora.ide.preferencepages.BuildLevelPage;

public class UncertainLocalValidator extends AbstractValidator {
	private int level;

	public UncertainLocalValidator(IFile file) {
		super(file);
		level = BuildLevelPage.getBuildLevel(AuroraBuilder.CONFIG_PROBLEM);
	}

	public UncertainLocalValidator() {
	}

	@Override
	public AbstractProcessor[] getMapProcessor() {
		return new AbstractProcessor[] { new AbstractProcessor() {

			@Override
			public void processMap(IFile file, CompositeMap map, IDocument doc) {
				if (level == 0)
					return;
				if (map.getName().equalsIgnoreCase("path-config")) {
					int line = map.getLocationNotNull().getStartLine();
					for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
						String key = (String) entry.getKey();
						String value = map.getString(key);
						IRegion region = getValueRegion(doc, line - 1, key,
								value);
						File f = new File(value);
						if (f.exists()) {
							if (!f.isDirectory())
								AuroraBuilder.addMarker(file, key + " : "
										+ value + " 存在 , 但不是一个目录", line,
										region, level,
										AuroraBuilder.CONFIG_PROBLEM);
						} else
							AuroraBuilder.addMarker(file, key + " : " + value
									+ " 不存在 ", line, region, level,
									AuroraBuilder.CONFIG_PROBLEM);
					}
				}
			}

			@Override
			public void processComplete(IFile file, CompositeMap map,
					IDocument doc) {

			}
		} };
	}
}
