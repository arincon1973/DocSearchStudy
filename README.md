Classes to provide search mechanisms to find a pattern is a set of files:

DocSearch: A program that performs string matching on a set of files. The files are loaded from list of files provided in a
configuration file.  The configuration file name is provided as the only parameter to the executable.

Performance Test: A program that performs a performance test on three different string matching algorithms: String Match, Regex and
 Preprocessed file.  It runs 2M searches with random search pattern inputs using the following assumptions:
 - The three algorithms are being used to find an exact match of text, so the patterns used didn’t include regular expression characters.
 - The search patterns include characters in the set {[a-z],[A-Z],[0-9],[ ]}
 - The search pattern is a string between 1 and 256 characters

The files are loaded from list of files provided in a configuration file.  The configuration file name is provided as
the only parameter to the executable. The configuration file is a properties file with one property: app.readers.
A sample configuration file is provided by fiel read.config
