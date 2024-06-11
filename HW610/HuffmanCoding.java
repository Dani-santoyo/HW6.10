package HW610;
import java.io.*;
import java.util.*;

class HuffmanNode {
    int frequency;
    char character;
    HuffmanNode left;
    HuffmanNode right;
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequency - y.frequency;
    }
}

public class HuffmanCoding {

    public static Map<Character, String> huffmanCodes = new HashMap<>();
    public static PriorityQueue<HuffmanNode> priorityQueue;

    public static void main(String[] args) throws IOException {
        String text = readFile("src/HW610/DeclarationOfIndependence.txt");
        Map<Character, Integer> frequencyMap = buildFrequencyMap(text);

        buildHuffmanTree(frequencyMap);
        generateCodes(priorityQueue.peek(), "");

        String compressedText = compressText(text);
        writeFile("Compressed.txt", compressedText);

        System.out.println("Original size: " + text.length() * 8 + " bits");
        System.out.println("Compressed size: " + compressedText.length() + " bits");
    }

    public static String readFile(String filePath) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        reader.close();
        return text.toString();
    }

    public static Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    public static void buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        priorityQueue = new PriorityQueue<>(new HuffmanComparator());
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.character = entry.getKey();
            node.frequency = entry.getValue();
            node.left = null;
            node.right = null;
            priorityQueue.add(node);
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode x = priorityQueue.poll();
            HuffmanNode y = priorityQueue.poll();

            HuffmanNode sum = new HuffmanNode();
            sum.frequency = x.frequency + y.frequency;
            sum.character = '-';
            sum.left = x;
            sum.right = y;
            priorityQueue.add(sum);
        }
    }

    public static void generateCodes(HuffmanNode root, String code) {
        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.character, code);
            return;
        }
        generateCodes(root.left, code + "0");
        generateCodes(root.right, code + "1");
    }

    public static String compressText(String text) {
        StringBuilder compressedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            compressedText.append(huffmanCodes.get(c));
        }
        return compressedText.toString();
    }

    public static void writeFile(String filePath, String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(text);
        writer.close();
    }
}

