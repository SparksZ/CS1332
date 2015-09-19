import java.util.*;

/**
 * The Huffman Encoder and Decoder Class
 * @author Zack Sparks
 * @version 1.0
 */
public class HuffmanEncoder {
    Node root;
    int sigmaSize;
    Map<Character, String> cipher = new HashMap<>();
    Map<String, Character> reverseCipher = new HashMap<>();

    public String encode(String text) {
        buildTree(text);
        makeCipher(root, "");
        return encoder(text);
    }

    /**
     * With the tree and cipher constructed, this actually does the encoding
     * @param text the text to be encoded
     * @return the encoded string
     */
    private String encoder(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            result.append(cipher.get(text.charAt(i)));
        }
        return result.toString();
    }

    /**
     * Traverses the tree and builds the cipher map
     * @param node the node to start the traversal at (begins with root, but is
     *             recursive)
     * @param s the code up to the node passed in
     */
    private void makeCipher(Node node, String s) {
        if (!node.isLeaf()) {
            makeCipher(node.left, s + '0');
            makeCipher(node.right, s + '1');
        } else {
            cipher.put(node.character, s);
        }
    }

    /**
     * Constructs the tree to be traversed from root to leaf in order to make
     * the cipher
     * @param text the text to be encoded
     */
    private void buildTree(String text) {
        Map<Character, Integer> freq = countFrequencies(text);
        PriorityQueue<Node> nodePriorityQueue = new PriorityQueue<>();

        for (Character c : freq.keySet()) {
            nodePriorityQueue.add(new Node(c, freq.get(c)));
        }

        while (nodePriorityQueue.size() > 1) {
            Node right = nodePriorityQueue.poll();
            Node left = nodePriorityQueue.poll();
            Node parent = new Node('0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;

            nodePriorityQueue.add(parent);
        }

        root = nodePriorityQueue.poll();
        sigmaSize = freq.size();
    }


    /**
     * This method counts the frequencies of each letter in the input string
     * @param text the uncoded text message to be encoded
     * @return a map of the character in the text (key) and their frequencies
     *         (key)
     */
    private Map<Character, Integer> countFrequencies(String text) {
        Map<Character, Integer> result = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);

            if (result.containsKey(letter)) {
                Integer freq = result.get(letter) + 1;

                result.put(letter, freq);
            } else {
                result.put(letter, 1);
            }
        }

        return result;
    }


    /**
     * Returns the decoded string
     * @param cipher the Map which has the characters and their coded
     *               counterparts
     * @param text The coded text to be deciphered
     * @return the decoded text
     */
    public String decode(Map<Character, String> cipher, String text) {
        createReverseCipher(cipher);

        return decoder(text);
    }

    /**
     * Workhorse that does the decoding
     * @param text the encoded text to be deciphered
     * @return the decoded text
     */
    private String decoder(String text) {
        Deque<Character> characters = new ArrayDeque<>();
        StringBuilder current = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            characters.addLast(text.charAt(i));
        }

        while (characters.size() > 0) {
            while (!reverseCipher.containsKey(current.toString())) {
                current.append(characters.removeFirst());
            }
            result.append(reverseCipher.get(current.toString()));
            current = new StringBuilder();
        }

        return result.toString();
    }

    /**
     * Creates a reverse cipher
     * @param cipher the Character to code cipher to be reversed
     */
    private void createReverseCipher(Map<Character, String> cipher) {
        Set<Map.Entry<Character, String>> entries = cipher.entrySet();

        for (Map.Entry<Character, String> e : entries) {
            reverseCipher.put(e.getValue(), e.getKey());
        }
    }

    /**
     * @return the character to code cipher
     */
    public Map<Character, String> getCipher() {
        return cipher;
    }

    /**
     * The Node class for the huffman tree
     */
    private class Node implements Comparable<Node> {
        Node left;
        Node right;
        char character;
        int frequency;

        public Node(char c, int freq) {
            character = c;
            frequency = freq;
        }

        @Override
        public int compareTo(Node o) {
            return frequency - o.frequency;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }
}
