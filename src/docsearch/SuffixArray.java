package docsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SuffixArray
{
    private String[] text;
    private int length;
    private int[] index;
    private String[] suffix;

    public SuffixArray(String text)
    {
        this.text = new String[text.length()];

        for (int i = 0; i < text.length(); i++)
        {
            this.text[i] = text.substring(i, i+1);
        }

        this.length = text.length();
        this.index = new int[length];
        for (int i = 0; i < length; i++)
        {
            index[i] = i;
        }

        suffix = new String[length];
    }

    public void createSuffixArray()
    {
        for(int index = 0; index < length; index++)
        {
            String text = "";
            for (int text_index = index; text_index < length; text_index++)
            {
                text+=this.text[text_index];
            }
            suffix[index] = text;
        }

    }


    public static void main(String...arg)throws IOException
    {
        String text = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the Text String ");
        text = reader.readLine();

        SuffixArray suffixarray = new SuffixArray(text);
        suffixarray.createSuffixArray();
    }

    public int search(String searchTerm) {
        if (searchTerm == null || searchTerm.length() == 0) {
            return 0;
        }

        int count = 0;
        char firstPatternChar = searchTerm.charAt(0);
        for (int iterate = 0; iterate < length; iterate++)
        {
            char firstSubstrChar = suffix[iterate].charAt(0);
            if (firstPatternChar <= firstSubstrChar) {
                if (firstPatternChar == firstSubstrChar) {
                    if (suffix[iterate].startsWith(searchTerm)) {
                        count = count + 1;
                    }
                }
            }
        }
        return count;
    }
}