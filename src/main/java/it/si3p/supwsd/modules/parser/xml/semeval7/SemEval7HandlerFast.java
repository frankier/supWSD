package it.si3p.supwsd.modules.parser.xml.semeval7;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.parser.xml.XMLHandler;

/**
 * @author papandrea
 *
 */
public class SemEval7HandlerFast extends XMLHandler {

	protected String mInstanceID, mSentenceID,mSentence;
	private final List<Annotation>mAnnotations;
	protected String mInstance="",mLemma,mPOS;
	protected final List<Lexel>mLexels;
	
	public SemEval7HandlerFast() {

		mLexels = new ArrayList<Lexel>();
		mAnnotations=new ArrayList<Annotation>();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {

		SemEval7Tag tag;

		tag = SemEval7Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case INSTANCE:

			mInstance="";
			mInstanceID = attributes.getValue(SemEval7Attribute.ID.name().toLowerCase());
			mLemma=attributes.getValue(SemEval7Attribute.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attribute.POS.name().toLowerCase());
			break;

		case SENTENCE:

			mSentence = "";
			mSentenceID= attributes.getValue(SemEval7Attribute.ID.name().toLowerCase());
			break;
			
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval7Tag tag = SemEval7Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case TEXT:

			notifyAnnotations();
			break;
			
		case SENTENCE:

			addAnnotation();
			break;

		case INSTANCE:
			
			addWord(formatAnnotation(mInstance));
			addInstance(formatInstance(mLemma)+"."+mPOS);
			break;

		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) {

		String word;

		switch ((SemEval7Tag) this.get()) {

		case SENTENCE:

			word = new String(ch, start, length).replaceAll("[\r\n]", " ");
			addWord(word);
			break;

		case INSTANCE:
			
			word = new String(ch, start, length).replaceAll("[\r\n]", " ");
			mInstance+=word;
			break;
	
		default:
			break;
		}
	}

	protected final void addWord(String word) {

		word=word.trim();
		
		if (!word.isEmpty()) 
			mSentence += word+" ";		
	}
	
	protected final Lexel addInstance(String instance) {

		Lexel lexel;
		
		lexel=new Lexel(mInstanceID, instance.trim());
		mLexels.add(lexel);
		
		return lexel;
	}

	protected final String formatInstance(String lemma){
		
		return lemma.trim().replaceAll("[\\s\\-]", "_").toLowerCase();
	}
	
	protected void notifyAnnotations() throws SAXException {
		
		try {
			mAnnotationListener.notifyAnnotations(mAnnotations);
		} catch (Exception e) {
			throw new SAXException(e);
		}
		
		mAnnotations.clear();
	}

	protected void addAnnotation() {

		Annotation annotation;

		annotation=new Annotation(mSentenceID,mSentence.trim());		
		annotation.addLexels(mLexels);		
		mAnnotations.add(annotation);
		mLexels.clear();	
	}
}
