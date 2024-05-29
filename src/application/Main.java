package application;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	TextField filePathTF = new TextField();
	Button browseBtn = new Button("Browse...");
	Button compressBtn = new Button("Compress");
	Button decompressBtn = new Button("Decompress");
	Button headerBtn = new Button("Header");
	Button statsBtn = new Button("Stats");
	Stage alertStage = new Stage();
	Button okBtn = new Button();
	TextArea infoTA = new TextArea();
	int flag = 0;
	LinkedList<Code> codeList;

	// List used to display stats

	// Display header
	public void headerBtnAction() {
		infoTA.appendText(
				"Header structure: \nSignature + Extension + . +  3 Bits for last byte + 13 Bits for tree size in bits + Encoded Tree\n");
		infoTA.appendText("Header: \n" + Huffman.headerSave + "\n");
		infoTA.appendText("---------------------------------\n");
		infoTA.setScrollLeft(0);
		infoTA.setScrollTop(0);
	}

	// Display node frequencies for last compressed file
	public void statsBtnAction() {

		// Set measurements for stage
		double cellWidth = 80;
		double cellHeight = 40;
		double height = (cellHeight * 4) + 56;
		double width = (cellWidth * Huffman.frequencyListSave.length) + 55;
		if (Huffman.frequencyListSave.length > 10) {
			width = (cellWidth * 10);
		}

		// Setting up panes
		ScrollPane sp = new ScrollPane();
		GridPane gp = new GridPane();

		sp.setStyle("-fx-background-color: #282828;");

		// Centering component alignment within cells
		gp.setStyle("-fx-background-color: #282828;");
		gp.setGridLinesVisible(true);
		GridPane.setHalignment(gp, HPos.CENTER);
		GridPane.setValignment(gp, VPos.CENTER);

		sp.setContent(gp);

		// Setting up stage and scene
		Scene scene = new Scene(sp);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.setScene(scene);

		// Stage size
		stage.setHeight(height);
		stage.setWidth(width);

		// Set up rows and columns for GridPane
		ColumnConstraints[] gpColumn = new ColumnConstraints[Huffman.frequencyListSave.length + 1];
		RowConstraints[] gpRow = new RowConstraints[4];

		for (int i = 0; i < 4; i++) {
			gpRow[i] = new RowConstraints();
			gpRow[i].setValignment(VPos.BOTTOM);
			gpRow[i].setMaxHeight(cellHeight);
			gpRow[i].setMinHeight(cellHeight);
			gpRow[i].setPrefHeight(cellHeight);
			gp.getRowConstraints().add(gpRow[i]);
		}

		for (int i = 0; i < gpColumn.length; i++) {
			gpColumn[i] = new ColumnConstraints();
			gpColumn[i].setHalignment(HPos.CENTER);
			gpColumn[i].setMaxWidth(cellWidth);
			gpColumn[i].setMinWidth(cellWidth);
			gpColumn[i].setPrefWidth(cellWidth);
			gp.getColumnConstraints().add(gpColumn[i]);
		}

		// Set up components to fill GridPane
		Label[] byteValueLbl = new Label[Huffman.frequencyListSave.length + 1];
		Label[] frequencyLbl = new Label[Huffman.frequencyListSave.length + 1];
		Label[] huffCodesLbl = new Label[Huffman.frequencyListSave.length + 1];
		Label[] characterLbl = new Label[Huffman.frequencyListSave.length + 1];

		// Labels for each row
		byteValueLbl[0] = new Label("Value");
		frequencyLbl[0] = new Label("Freq");
		huffCodesLbl[0] = new Label("Huffman Code");
		characterLbl[0] = new Label("Character");

		characterLbl[0].setTextFill(Color.WHITE);
		frequencyLbl[0].setTextFill(Color.WHITE);
		byteValueLbl[0].setTextFill(Color.WHITE);
		huffCodesLbl[0].setTextFill(Color.WHITE);

		GridPane.setConstraints(characterLbl[0], 0, 0);
		GridPane.setConstraints(frequencyLbl[0], 0, 1);

		GridPane.setConstraints(byteValueLbl[0], 0, 2);
		GridPane.setConstraints(huffCodesLbl[0], 0, 3);

		gp.getChildren().addAll(characterLbl[0], frequencyLbl[0], byteValueLbl[0], huffCodesLbl[0]);

		// Labels for each cell and their properties
		for (int i = 0; i < Huffman.frequencyListSave.length; i++) {
			byte[] byteArray = { (Huffman.frequencyListSave[i].value) };

			frequencyLbl[i + 1] = new Label("" + Huffman.frequencyListSave[i].freq);
			huffCodesLbl[i + 1] = new Label("" + codeList.get(i).prefix);
			String temp = new String(byteArray, Charset.forName("UTF-8"));
			byteValueLbl[i + 1] = new Label("" + (int) temp.charAt(0));

			if (temp.equals(" "))
				characterLbl[i + 1] = new Label("Space");
			else if (temp.equals("\n"))
				characterLbl[i + 1] = new Label("NewLine");
			else if (temp.equals("\r"))
				characterLbl[i + 1] = new Label("Return");
			else
				characterLbl[i + 1] = new Label(temp);

			characterLbl[i + 1].setTextFill(Color.WHITE);
			frequencyLbl[i + 1].setTextFill(Color.WHITE);
			byteValueLbl[i + 1].setTextFill(Color.WHITE);
			huffCodesLbl[i + 1].setTextFill(Color.WHITE);

			GridPane.setConstraints(characterLbl[i + 1], i + 1, 0);
			GridPane.setConstraints(frequencyLbl[i + 1], i + 1, 1);
			GridPane.setConstraints(byteValueLbl[i + 1], i + 1, 2);
			GridPane.setConstraints(huffCodesLbl[i + 1], i + 1, 3);

			gp.getChildren().addAll(characterLbl[i + 1], frequencyLbl[i + 1], byteValueLbl[i + 1], huffCodesLbl[i + 1]);
		}

		stage.getIcons().add(new Image(Huffman.class.getResourceAsStream("resources/images/icon.png")));

		stage.setTitle("Stats");
		stage.show();
	}

	public void browseBtnAction() {

		// Choose file
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);

		// Check if file is null
		if (selectedFile == null) {
			return;
		}

		// Get extension
		String selectedFilePath = selectedFile.getAbsolutePath();
		String extension = selectedFilePath.substring(selectedFilePath.lastIndexOf("."));

		// In case of extension being .huff, enable decompressBtn, else disable it
		if (extension.compareTo(".huff") == 0) {
			decompressBtn.setDisable(false);
		} else {
			decompressBtn.setDisable(true);
		}

		// Add file path into filePathTF
		filePathTF.setText(selectedFile.getAbsolutePath());
	}

	public void compressBtnAction() throws IOException {

		compressBtn.setDisable(true);
		browseBtn.setDisable(true);

		// Get filePath from TextField
		String filePath = filePathTF.getText();

		// Check if TextField is empty, if empty return
		if (filePath.compareTo("") == 0) {
			compressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("File does not exist");
			infoTA.setText(infoTA.getText() + "File does not exist\n");
			compressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		Path path = Paths.get(filePath);
		if (Files.size(path) == 0) {
			System.out.println("File is too small");
			infoTA.setText(infoTA.getText() + "File is empty!\n");
			compressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		// Choose directory
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDirectory = dc.showDialog(null);

		// Check if directory was chosen
		if (selectedDirectory == null) {
			System.out.println("No directory chosen");
			compressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		String directoryPath = selectedDirectory.getAbsolutePath();

		// Get specific file path
		String destination = directoryPath + "\\" + fileName + ".huff";

		// Destination file must be a new file
		File destinationFile = new File(destination);
		for (int i = 1; destinationFile.exists(); i++) {
			destination = directoryPath + "\\" + fileName + " (" + i + ")" + ".huff";
			destinationFile = new File(destination);
		}
		infoTA.setText("");
		infoTA.appendText("---------------------------------\n");
		infoTA.appendText("Compressing...\n" + "From: \n" + filePathTF.getText() + "\nTo: \n" + destination + "\n");
		infoTA.appendText("Finding byte frequencies...\n");
		// Find byte frequencies
		int[] frequency = Huffman.findByteFrequencies(filePathTF.getText());

		// Trim down byte frequencies to the used ones and move them into nodes
		Node[] freq = Huffman.trimFrequency(frequency);

		Huffman.frequencyListSave = new Node[freq.length];
		for (int i = 0; i < Huffman.frequencyListSave.length; i++) {
			Huffman.frequencyListSave[i] = new Node(freq[i].freq, freq[i].value, freq[i].isUsed);
		}

		infoTA.appendText("Building tree...\n");
		// Build tree from trimmed frequencies
		Node tree = Huffman.buildHuffmanTree(freq);

		infoTA.appendText("Getting codes...\n");

		// Get list of codes out of tree

		codeList = Huffman.getCodes(tree);

		// Calculate expected number of bits from frequency list and code list
		long expBits = Huffman.calculateBits(frequency, codeList);

		infoTA.appendText("Building header...\n");
		// Build header
		String header = Huffman.buildHeader(expBits, tree, filePathTF.getText(), destination);

		infoTA.appendText("Writing file contents...\n");
		// Compress file contents and write header
		Huffman.compressFile(codeList, filePathTF.getText(), destination, expBits, header);

		compressBtn.setDisable(false);
		statsBtn.setDisable(false);
		headerBtn.setDisable(false);
		browseBtn.setDisable(false);

		infoTA.appendText("Done!\n");
		double ratio = 100 - ((double) destinationFile.length() / file.length() * 100);
		infoTA.appendText("Size before: " + file.length() + " bytes\nSize after: " + destinationFile.length()
				+ " bytes\n" + "Compression ratio : " + ratio + "%\n");
		infoTA.appendText("---------------------------------\n");
		infoTA.setScrollLeft(0);
		infoTA.setScrollTop(0);
		statsBtn.setDisable(false);
		headerBtn.setDisable(false);
	}

	public void decompressBtnAction() throws IOException {

		compressBtn.setDisable(true);
		statsBtn.setDisable(true);
		headerBtn.setDisable(true);
		decompressBtn.setDisable(true);
		browseBtn.setDisable(true);

		// Get file to be decompressed
		String fromFilePath = filePathTF.getText();

		// Check if file exists(could be deleted after selection)
		File selectedFile = new File(fromFilePath);
		if (!selectedFile.exists()) {

			if (Huffman.headerSave.compareTo("") != 0) {
				statsBtn.setDisable(false);
				headerBtn.setDisable(false);
			}
			compressBtn.setDisable(false);
			decompressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		String fileName = selectedFile.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf(".") + 1);

		// Select directory
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDirectory = dc.showDialog(null);

		// Check if directory was chosen
		if (selectedDirectory == null) {
			if (Huffman.headerSave.compareTo("") != 0) {
				statsBtn.setDisable(false);
				headerBtn.setDisable(false);
			}
			compressBtn.setDisable(false);
			decompressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		if (!selectedDirectory.exists()) {
			if (Huffman.headerSave.compareTo("") != 0) {
				statsBtn.setDisable(false);
				headerBtn.setDisable(false);
			}
			compressBtn.setDisable(false);
			decompressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}

		String toDirectoryPath = selectedDirectory.getAbsolutePath() + "\\" + fileName;

		try {
			Huffman.decompressFile(fromFilePath, toDirectoryPath);
		} catch (IllegalArgumentException e) {
			alert();
			if (Huffman.headerSave.compareTo("") != 0) {
				statsBtn.setDisable(false);
				headerBtn.setDisable(false);
			}
			compressBtn.setDisable(false);
			decompressBtn.setDisable(false);
			browseBtn.setDisable(false);
			return;
		}
		infoTA.setText("");
		infoTA.appendText("---------------------------------\n");
		infoTA.appendText("Decompressing...\n" + "From: \n" + filePathTF.getText() + "\nTo: \n" + toDirectoryPath
				+ Huffman.extensionSave + "\n");

		infoTA.appendText("Done!\n");
		File before = new File(filePathTF.getText());
		File after = new File(toDirectoryPath + Huffman.extensionSave);

		infoTA.appendText("Size before: " + before.length() + " bytes\nSize after: " + after.length() + " bytes\n");
		infoTA.appendText("---------------------------------\n");

		decompressBtn.setDisable(false);
		compressBtn.setDisable(false);
		browseBtn.setDisable(false);
	}

	public void start(Stage stage) throws Exception {

		AnchorPane root = new AnchorPane();

		decompressBtn.setLayoutX(439);
		decompressBtn.setLayoutY(204);
		decompressBtn.setStyle(
				"-fx-background-color: transparent; -fx-border-color: green; -fx-border-width: 3px; -fx-text-fill: white;");
		decompressBtn.setOnAction(e -> {
			try {
				decompressBtnAction();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		decompressBtn.setFont(new Font("System Bold", 15.0));

		headerBtn.setLayoutX(439);
		headerBtn.setLayoutY(260);
		headerBtn.setStyle(
				"-fx-background-color: transparent; -fx-border-color: green; -fx-border-width: 3px; -fx-text-fill: white;");
		headerBtn.setOnAction(e -> headerBtnAction());
		headerBtn.setFont(new Font("System Bold", 15.0));
		headerBtn.setDisable(true);

		filePathTF.setLayoutX(27);
		filePathTF.setLayoutY(68);
		filePathTF.setPrefHeight(31);
		filePathTF.setPrefWidth(405);

		browseBtn.setLayoutX(439);
		browseBtn.setLayoutY(68);
		browseBtn.setStyle(
				"-fx-background-color: transparent; -fx-border-color: green; -fx-border-width: 3px; -fx-text-fill: white;");
		browseBtn.setOnAction(e -> browseBtnAction());
		browseBtn.setFont(new Font("System Bold", 15.0));

		compressBtn.setLayoutX(439);
		compressBtn.setLayoutY(150);
		compressBtn.setStyle(
				"-fx-background-color: transparent; -fx-border-color: green; -fx-border-width: 3px; -fx-text-fill: white;");
		compressBtn.setOnAction(e -> {
			try {
				compressBtnAction();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		compressBtn.setFont(new Font("System Bold", 15.0));

		statsBtn.setLayoutX(439);
		statsBtn.setLayoutY(315);
		statsBtn.setStyle(
				"-fx-background-color: transparent; -fx-border-color: green; -fx-border-width: 3px; -fx-text-fill: white;");
		statsBtn.setOnAction(e -> statsBtnAction());
		statsBtn.setFont(new Font("System Bold", 15.0));
		statsBtn.setDisable(true);

		infoTA.setLayoutX(28);
		infoTA.setLayoutY(152);
		infoTA.setPrefHeight(194);
		infoTA.setPrefWidth(392);

		root.getChildren().addAll(decompressBtn, headerBtn, filePathTF, browseBtn, compressBtn, statsBtn, infoTA);
		// .setStyle("-fx-fill: linear-gradient(to bottom right, rgb(50, 50, 50) 0%,
		// black 100%);");
		Scene scene = new Scene(root, 569, 375);
		// root.setStyle("-fx-background-color: #282828;");
		// root.setStyle("-fx-background-color: linear-gradient(to bottom right, rgb(50,
		// 50, 50) 0%, black 100%);");
		stage.setTitle("Huffman Coding");
		stage.getIcons().add(new Image(Huffman.class.getResourceAsStream("resources/images/icon.png")));
		stage.setResizable(false);
		root.setStyle("-fx-background-image: url(\"icon.png\"); " + "-fx-background-size: cover;");
		stage.setScene(scene);
		stage.show();

	}

	public void alert() throws IOException {

		if (flag == 0) {

			AnchorPane root = new AnchorPane();

			Label label = new Label("Selected file is corrupted and cannot be decompressed");
			label.setLayoutX(92);
			label.setLayoutY(76);
			label.setTextFill(Color.WHITE);

			okBtn.setLayoutX(245);
			okBtn.setLayoutY(141);
			okBtn.setStyle("-fx-background-color: #101010;");
			okBtn.setTextFill(Color.WHITE);
			okBtn.setOnAction(e -> okBtnAction());

			root.getChildren().addAll(label, okBtn);

			Scene scene = new Scene(root, 530, 202);
			scene.setFill(Color.web("#282828"));

			alertStage.setScene(scene);
			alertStage.show();

			alertStage.setScene(scene);
			alertStage.setTitle("Invalid Header!");
			alertStage.getIcons()
					.add(new Image(Huffman.class.getResourceAsStream("resources/images/Notification.png")));
			alertStage.setResizable(false);
			alertStage.initModality(Modality.APPLICATION_MODAL);
			flag = 1;
		}
		alertStage.show();
	}

	public void okBtnAction() {
		((Stage) (okBtn.getScene().getWindow())).hide();
		flag = 1;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
