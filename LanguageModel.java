import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		// Your code goes here
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {	
		CharData[] charDataArray = probs.toArray();
        CharData current, last;
        double probsSize = probs.getLength();
        CharData first = probs.getFirst();
        first.p = first.count / probsSize;
        first.cp = first.p;

        for(int i = 1; i < charDataArray.length; i++) {
            current = charDataArray[i];
            last = charDataArray[i -1];
            current.p = current.count / probsSize;
            current.cp = last.cp + current.p;
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
		double r = randomGenerator.nextDouble();;
        CharData[] probsArray = probs.toArray();
        for(int i = 0; i < probsArray.length; i++) {
            if(probsArray[i].cp > r ) {
                return probsArray[i].chr;
            }
        }

        return probsArray[probsArray.length - 1].chr;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		String substr = initialText.substring(initialText.length() - 1 - textLength, initialText.length() - 1);
        boolean isSubStrExist = CharDataMap.keySet().contains(substr);
        if (!isSubStrExist) {
            return initialText;
        } else {
            String newStr = initialText + " ";
            List charList = CharDataMap.get(substr);
            for(int i = 0; i < textLength; i++) {
                newStr += getRandomChar(charList);
            }
            return newStr;
        }
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		// Your code goes here
    }
}
