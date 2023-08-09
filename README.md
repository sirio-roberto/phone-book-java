# PhoneBook Application

Welcome to the PhoneBook Application! This Java application provides various search algorithms to find entries in a phonebook.

### Getting Started

To get started with the PhoneBook Application, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/phonebook.git
   cd phonebook
2. Compile and run the application:
   ```sh
   javac phonebook/Main.java
   java phonebook.Main
   
### Features

The PhoneBook Application offers the following features:

- Linear search 
- Hash table search 
- Quick sort + binary search 
- Bubble sort + jump search

### Sample Data

The application uses two files for its operations:

1. `find.txt`: Contains names to find in the phonebook. 
2. `medium_directory.txt`: Contains contacts and phone numbers.

Feel free to replace these files with your own data or customize the application accordingly.
## Output Example

```lua
Here's an example of the output you might see after running the application:
Start searching (linear search)...
Found 43 / 500 entries. Time taken: 0 min. 0 sec. 643 ms.

Start searching (hash table)...
Found 43 / 500 entries. Time taken: 0 min. 0 sec. 22 ms.
Creating time: 0 min. 0 sec. 21 ms.
Searching time: 0 min. 0 sec. 1 ms.

Start searching (quick sort + binary search)...
Found 43 / 500 entries. Time taken: 0 min. 0 sec. 86 ms.
Sorting time: 0 min. 0 sec. 85 ms.
Searching time: 0 min. 0 sec. 1 ms.

Start searching (bubble sort + jump search)...
Found 43 / 500 entries. Time taken: 7 min. 13 sec. 404 ms.
Sorting time: 7 min. 13 sec. 396 ms.
Searching time: 0 min. 0 sec. 8 ms.```
```

### Contributing

Contributions are welcome! If you find a bug, have suggestions, or want to add new features, follow these steps:

1. Fork the repository. 
2. Create a new branch: `git checkout -b feature-name` 
3. Make your changes and commit them: `git commit -am 'Add some feature'` 
4. Push to the branch: `git push origin feature-name` 
5. Create a pull request.

### License

This project is licensed under the MIT License.