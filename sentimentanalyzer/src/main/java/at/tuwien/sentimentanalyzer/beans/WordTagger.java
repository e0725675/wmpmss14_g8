package at.tuwien.sentimentanalyzer.beans;

import java.io.OutputStream;
import java.io.PrintStream;

import at.tuwien.sentimentanalyzer.entities.Message;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*
 * Author: Pia 
 * Using the Stanford Core NLP Library
 * Tagging of words, see list of abbreviations at the end
 */

public class WordTagger {
	
	public Message addWordtype (Message msg){
	//public HashMap<String, List<String>> addWordtype (HashMap<String, List<String>> csv){ //List<String> textList
		//List<String> taggedList = new ArrayList<String>();
		// trick to disable annoying printouts
		// this is your print stream, store the reference
		PrintStream err = System.err;
		PrintStream out = System.out;
		
		// now make all writes to the System.err stream silent 
		System.setErr(new PrintStream(new OutputStream() {
		    public void write(int b) {
		    }
		}));
		System.setOut(new PrintStream(new OutputStream() {
		    public void write(int b) {
		    }
		}));

				// YOUR CODE HERE
		
		
		MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		
		//if(csv.size() < 1)
		//	return null;
		if (msg == null || msg.getMessage() == null)
			return null;
		
		//for(Map.Entry<String, List<String>> entry: csv.entrySet())
		//	entry.getValue().set(1, tagger.tagString(entry.getValue().get(1)));
		msg.setMessage(tagger.tagString(msg.getMessage()));
		
		//return csv;
		
		
		// set everything back to its original state afterwards
		System.setErr(err);
		System.setOut(out);
		return msg;
	}
}

/*
Tag list
----------------------------------------------
CC 	Coordinating conjunction e.g. and,but,or...
CD 	Cardinal Number
DT 	Determiner
EX 	Existential there
FW 	Foreign Word
IN 	Preposision or subordinating conjunction
JJ 	Adjective
JJR 	Adjective, comparative
JJS 	Adjective, superlative
LS 	List Item Marker
MD 	Modal e.g. can, could, might, may...
NN 	Noun, singular or mass
NNP 	Proper Noun, singular
NNPS 	Proper Noun, plural
NNS 	Noun, plural
PDT 	Predeterminer e.g. all, both ... when they precede an article
POS 	Possessive Ending e.g. Nouns ending in 's
PRP 	Personal Pronoun e.g. I, me, you, he...
PRP$ 	Possessive Pronoun e.g. my, your, mine, yours...
RB 	Adverb, Most words that end in -ly as well as degree words like quite, too and very
RBR 	Adverb, comparative, Adverbs with the comparative ending -er, with a strictly comparative meaning.
RBS 	Adverb, superlative
RP 	Particle
SYM 	Symbol, Should be used for mathematical, scientific or technical symbols
TO 	to
UH 	Interjection e.g. uh, well, yes, my...
VB 	Verb, base form, subsumes imperatives, infinitives and subjunctives
VBD 	Verb, past tense, includes the conditional form of the verb to be
VBG 	Verb, gerund or persent participle
VBN 	Verb, past participle
VBP 	Verb, non-3rd person singular present
VBZ 	Verb, 3rd person singular present
WDT 	Wh-determiner e.g. which, and that when it is used as a relative pronoun
WP 	Wh-pronoun e.g. what, who, whom...
WP$ 	Possessive wh-pronoun
WRB 	Wh-adverb e.g. how, where why 
*/