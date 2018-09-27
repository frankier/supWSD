package it.si3p.supwsd.inventory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.babelscape.util.UniversalPOS;

import it.si3p.supwsd.data.POSMap.TAG;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetComparator;
import it.uniroma1.lcl.jlt.util.Language;

/**
 * @author papandrea
 *
 */
class BabelNetInventory implements SenseInventory {

	private final BabelNet mBabelNet;
	private final Language mLang;
	
	BabelNetInventory(String ISO) throws IOException {

		mBabelNet = BabelNet.getInstance();
		mLang=Language.fromISO(ISO==null?"EN":ISO);
	}

	@Override
	public String getSense(String lemma, TAG pos) {

		String sense = null;
		List<BabelSynset> synsets;

		try {
			synsets = mBabelNet.getSynsets(lemma, mLang, getPOS(pos));
			Collections.sort(synsets, new BabelSynsetComparator(lemma));

			if (!synsets.isEmpty())
				sense = synsets.get(0).getID().getID();
			
		} catch (Exception e) {
	
		}

		return sense;
	}

	private UniversalPOS getPOS(TAG pos) {

		UniversalPOS POS;

		switch (pos) {

		case n:
			POS = UniversalPOS.NOUN;
			break;
			
		case v:
			POS = UniversalPOS.VERB;
			break;
			
		case a:
			POS = UniversalPOS.ADJ;
			break;
			
		case r:
			POS = UniversalPOS.ADV;
			break;
			
		default:
			POS = UniversalPOS.NOUN;
			break;
		}

		return POS;
	}

	public void dispose() {

	}

	@Override
	public void close() {

	}
}
