package org.libertyeiffel.eclipse.editor.editor;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class EiffelSourceViewerConfiguration extends SourceViewerConfiguration {

	public EiffelSourceViewerConfiguration() {
		super();
	}
	
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		
		return EiffelEditorPlugin.MY_PARTITIONING;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE, EiffelPartitionScanner.MULTILINE_COMMENT,
				EiffelPartitionScanner.SINGLELINE_COMMENT, EiffelPartitionScanner.STRING
		};
	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		MonoReconciler reconciler = new MonoReconciler(new EiffelReconcilingStrategy(), true);
		reconciler.install(sourceViewer);
		
		return reconciler;
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer viewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer repairer = new DefaultDamagerRepairer(new 
				EiffelCodeScanner(new EiffelColorProvider()));
		reconciler.setDamager(repairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(repairer, IDocument.DEFAULT_CONTENT_TYPE);
//		reconciler.setRepairer(repairer, EiffelPartitionScanner.SINGLELINE_COMMENT);
//		reconciler.setRepairer(repairer, EiffelPartitionScanner.MULTILINE_COMMENT);
//		reconciler.setRepairer(repairer, EiffelPartitionScanner.STRING);
		
		return reconciler;
	}
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		IAutoEditStrategy strategy = new DefaultIndentLineAutoEditStrategy();
		
		return new IAutoEditStrategy[] {strategy};
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor processor = new EiffelContentAssistantProcessor();
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		
		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		assistant.setContentAssistProcessor(new EiffelContentAssistantProcessor(),
				IDocument.DEFAULT_CONTENT_TYPE);
		
		assistant.setAutoActivationDelay(100);
		assistant.enableAutoActivation(true);
		assistant.enableAutoInsert(true);
		
		assistant.setProposalSelectorBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		return assistant;
	}
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		
		return new EIffelTextHover();
	}
	
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer viewer) {
		return new EiffelAnnotationHOver();
	}
	
	@Override
	public IUndoManager getUndoManager(ISourceViewer sourceViewer) {
		return null;
	}
}
