package com.pcg.sharedplatform;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	public static ExpPlatform platform;

	@FXML
	public AnchorPane anchorPane;
	@FXML
	public Label instruction;
	@FXML
	public JFXToggleButton phoneType;
	@FXML
	public JFXButton startButton;
	@FXML
	public JFXButton nextButton;
	@FXML
	public JFXButton retryButton;
	@FXML
	public JFXButton exprChooser;
	@FXML
	public JFXButton connectNet;
	@FXML
	public JFXButton disconnectNet;
	@FXML
	public JFXButton startExpr;
	@FXML
	public Label IPLabel;
	@FXML
	public JFXTextField portInput;
	@FXML
	public JFXComboBox<String> clientIps;
	@FXML
	public JFXTextField userName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		platform = new ExpPlatform(this);
		connectNet.setOnMouseClicked(event -> {
			if (platform.startServer(Integer.valueOf(portInput.getText()))) {
				portInput.setDisable(true);
				connectNet.setDisable(true);
				startExpr.setDisable(false);
			}
			else {
				messageBox("端口被占用！");
			}
		});

		startExpr.setOnMouseClicked(event -> {
			if (platform.startExp()) {
				startExpr.setDisable(true);
				exprChooser.setDisable(true);
				nextButton.setDisable(false);
				clientIps.setDisable(true);
				phoneType.setDisable(true);
				userName.setDisable(true);
			};
		});

		startButton.setOnMouseClicked(event -> {
			startButton.setDisable(true);
			nextButton.setDisable(false);
			retryButton.setDisable(false);
			platform.startTask();
		});

		nextButton.setOnMouseClicked(event -> {
			platform.stopTask();
			if (platform.nextTask()) {
				startButton.setDisable(false);
				nextButton.setDisable(true);
				retryButton.setDisable(true);
			}
			else {
				startExpr.setDisable(false);
				exprChooser.setDisable(false);
				phoneType.setDisable(false);
				clientIps.setDisable(false);
				userName.setDisable(false);
				nextButton.setDisable(true);
				startButton.setDisable(true);
				retryButton.setDisable(true);
			}
		});

		retryButton.setOnMouseClicked(event -> {
			nextButton.setDisable(false);
			platform.retryTask();
		});

		exprChooser.setOnMouseClicked(event -> {
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("选择实验序列文件");
			File file = fileChooser.showOpenDialog(retryButton.getScene().getWindow());
			if (file != null) {
				platform.tasks.loadTasks(file);
				if (!platform.tasks.isLoaded()) {
					messageBox("未选择实验！");
				}
			}
			else {
				messageBox("实验序列文件不存在！");
			}
		});

		disconnectNet.setDisable(true);
		startExpr.setDisable(true);
		startButton.setDisable(true);
		nextButton.setDisable(true);
		retryButton.setDisable(true);
		phoneType.setSelected(true);
	}

	public void setServerIP(String ip) {
		IPLabel.setText(ip);
	}

	public void setInstruction(String inst) {
		instruction.setText(inst);
	}

	public String getUserName() {
		return userName.getText();
	}

	public String getPhoneType() {
		if (phoneType.isSelected()) {
			return "LITTLEV";
		}
		else {
			return "LONGISLAND";
		}
	}

	public boolean isConnected() {
		if ((clientIps == null) || (clientIps.getValue() == null) || (clientIps.getValue().length() <= 0)) {
			return false;
		}
		return true;
	}

	public String getSelectedClient() {
		return clientIps.getValue();
	}

	public void addWaitingClient(String ip) {
		clientIps.getItems().add(ip);
	}

	public void disableNext() {
		nextButton.setDisable(true);
	}

	public void messageBox(String message) {
		JFXPopup popup = new JFXPopup();
		AnchorPane pane = new AnchorPane();
		Label infoL = new Label(String.format(message));
		infoL.setTextFill(Color.web("#eeeeee"));
		infoL.setLayoutX(80);
		infoL.setLayoutY(40);
		pane.getChildren().add(infoL);
		pane.setPrefWidth(200);
		pane.setPrefHeight(100);
		pane.setStyle("-fx-background-color: #202023");
		pane.setEffect(new DropShadow(2d, 0d, +2d, Color.BLACK));
		popup.setContent(pane);

		popup.setPopupContainer(anchorPane);
		popup.setSource(nextButton);
		Platform.runLater(() -> popup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 100, -20));
	}
}
