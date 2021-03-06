package org.intellij.plugins.xsltDebugger;

import javax.annotation.Nonnull;

import org.intellij.lang.xpath.xslt.XsltSupport;
import org.intellij.lang.xpath.xslt.impl.XsltChecker;
import org.intellij.plugins.xsltDebugger.impl.XsltDebuggerEditorsProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import consulo.ui.image.Image;

/*
* Created by IntelliJ IDEA.
* User: sweinreuter
* Date: 03.03.11
*/
public class XsltBreakpointType extends XLineBreakpointType<XBreakpointProperties>
{
	@Nonnull
	public static XsltBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtension(XsltBreakpointType.class);
	}

	private final XsltDebuggerEditorsProvider myMyEditorsProvider1 = new XsltDebuggerEditorsProvider(XsltChecker.LanguageLevel.V1);
	private final XsltDebuggerEditorsProvider myMyEditorsProvider2 = new XsltDebuggerEditorsProvider(XsltChecker.LanguageLevel.V2);

	public XsltBreakpointType()
	{
		super("xslt-line-breakpoint-type", "XSLT Line Breakpoints");
	}

	@Override
	public XDebuggerEditorsProvider getEditorsProvider(@Nonnull XLineBreakpoint<XBreakpointProperties> breakpoint, @Nonnull Project project)
	{
		final XSourcePosition position = breakpoint.getSourcePosition();
		if(position == null)
		{
			return null;
		}

		final PsiFile file = PsiManager.getInstance(project).findFile(position.getFile());
		if(file == null)
		{
			return null;
		}

		final XsltChecker.LanguageLevel level = XsltSupport.getXsltLanguageLevel(file);
		if(level == XsltChecker.LanguageLevel.V1)
		{
			return myMyEditorsProvider1;
		}
		else if(level == XsltChecker.LanguageLevel.V2)
		{
			return myMyEditorsProvider2;
		}

		return null;
	}

	@Nonnull
	@Override
	public Image getEnabledIcon()
	{
		return XsltSupport.createXsltIcon(AllIcons.Debugger.Db_set_breakpoint);
	}

	@Nonnull
	@Override
	public Image getDisabledIcon()
	{
		return XsltSupport.createXsltIcon(AllIcons.Debugger.Db_disabled_breakpoint);
	}

	@Override
	public XBreakpointProperties createBreakpointProperties(@Nonnull VirtualFile file, int line)
	{
		return null;
	}
}
