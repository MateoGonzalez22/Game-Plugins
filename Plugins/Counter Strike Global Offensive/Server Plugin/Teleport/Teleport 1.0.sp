#include <sourcemod>
#include <sdktools>

#pragma semicolon 1
#pragma newdecls required




public Plugin myinfo = 
{
	name = "Teleport",
	author = "tute",
	description = "Teleport",
	version = "1.0",
	url = ""
};

public void OnPluginStart()
{	
	
	RegConsoleCmd("getpos", commandGetPos, "");
	RegConsoleCmd("savepos", commandSavePos, "");
	RegConsoleCmd("tp", commandTeleport, "");
	RegConsoleCmd("test", commandArr, "");
	RegConsoleCmd("list", commandList, "");
	createFile();
	PrintToServer("Plugin Teleport loaded!");
	
}


public Action commandGetPos(int client, int args){
	
	float pos[3];
	
	GetClientAbsOrigin(client, pos);
	
	
	PrintToChatAll("Posición: %f %f %f", pos[0], pos[1], pos[2]);
	
	return Plugin_Handled;
	
}

public Action commandSavePos(int client, int args){
	
	
	if (args == 1){
		
		char arg1[32];
		GetCmdArg(1, arg1, sizeof(arg1));
		
		savePosition(client, arg1);
		
	}
	
	return Plugin_Handled;
}

public Action commandTeleport(int client, int args){
	
	if (args == 1){
		
		char arg1[32];
		GetCmdArg(1, arg1, sizeof(arg1));
		
		
		char texto[64];
	
		Handle readFile = OpenFile("addons/sourcemod/data/Teleport/config.txt", "r", false, NULL_STRING);
		
		int i = 0;
		
		int lineas = countFileLines(readFile);
		
		
		readFile = OpenFile("addons/sourcemod/data/Teleport/config.txt", "r", false, NULL_STRING);
		
		while(i < lineas){
			
			
			ReadFileLine(readFile, texto, sizeof(texto));
			
			if(StrContains(texto, arg1, true) != -1){
				
				int igualPos = StrContains(texto, "=", true);
				
				char coords[64];
				char xs[32];
				char ys[32];
				char zs[32];
					
				Format(coords, sizeof(coords), "%s", texto[igualPos+2]);
				
				int primerEspacio = StrContains(coords, " ", true);
				
				SplitString(coords, " ", xs, sizeof(xs));
				
				Format(coords, sizeof(coords), "%s", coords[primerEspacio + 1]);
				
				int segundoEspacio = StrContains(coords, " ", true);
				
				SplitString(coords, " ", ys, sizeof(ys));
				
				Format(zs, sizeof(zs), "%s", coords[segundoEspacio + 1]);
				
				
				
				float x = StringToFloat(xs);
				float y = StringToFloat(ys);
				float z = StringToFloat(zs);
				float pos[3];
				pos[0] = x;
				pos[1] = y;
				pos[2] = z;
				
				TeleportEntity(client, pos, NULL_VECTOR, NULL_VECTOR);
				
			}
			
			i++;
		}
		
		
		CloseHandle(readFile);
		
		
			
		}
	
	
	return Plugin_Handled;
	
}



public Action commandList(int client, int args){
	
	
	char texto[64];

	Handle read = OpenFile("addons/sourcemod/data/Teleport/config.txt", "r", false, NULL_STRING);
	
	int i = 0;
	
	int lineas = countFileLines(read);
	ReplyToCommand(client, "%d", lineas);
	
	read = OpenFile("addons/sourcemod/data/Teleport/config.txt", "r", false, NULL_STRING);
	
	char nombre[32];

	ReplyToCommand(client, "Posiciones Guardadas: \n");
	
	while(i < lineas){
		
		ReadFileLine(read, texto, sizeof(texto));
		SplitString(texto, " ", nombre, sizeof(nombre));
		ReplyToCommand(client, "- %s", nombre);
		
		//Format(lugar, sizeof(lugar), "%s - ", nombre);
		//StrCat(lugares, sizeof(lugares), lugar);
		
		i++;
	}
	
	CloseHandle(read);
	//ReplyToCommand(client, "Posiciones Guardadas: \n%s", lugares);
}


public Action commandArr(int client, int args){
	
	char ip[32];
	GetClientIP(client, ip, sizeof(ip), false);
	
	ReplyToCommand(client, "IP: %s", ip);
	
	NetFlow flow = NetFlow_Both;
	
	float latency = GetClientLatency(client, flow);
	
	ReplyToCommand(client, "latenia: %f", latency);
	
	float inAmount;
	float outAmount;
	
	GetServerNetStats(inAmount, outAmount);
	
	ReplyToCommand(client, "in: %f out: %f", inAmount, outAmount);
	
	
	int clients[1];
	clients[0] = client;
	
	ReplyToCommand(client, "loss: %f", GetClientAvgLoss(client, flow));
	
	
}



public void createFile(){
	

	CreateDirectory("addons/sourcemod/data/Teleport", 3);

	Handle file = OpenFile("addons/sourcemod/data/Teleport/config.txt", "a", false, NULL_STRING);
	
	if (file == INVALID_HANDLE){
		PrintToServer("[SERVER] Handle file returned in INVALID_HANDLE");
	}
	
	CloseHandle(file);
	
}





public int countFileLines(Handle readFile){
	int i = 0;
	char texto[64];
	
	while(!IsEndOfFile(readFile) && i < 200){
		
		ReadFileLine(readFile, texto, sizeof(texto));
		i++;
	
	}
	
	int lineas = i - 1;
	
	CloseHandle(readFile);
	
	return lineas;
}





public void savePosition(int cliente, char[] arg){
	
	float position[3];
	char texto[64];
	
	GetClientAbsOrigin(cliente, position);
	
	Format(texto, sizeof(texto), "%s = %f %f %f\n", arg, position[0], position[1], position[2]);
	
	Handle writeFile = OpenFile("addons/sourcemod/data/Teleport/config.txt", "a", false, NULL_STRING);
	
	WriteFileString(writeFile, texto, false);
	
	CloseHandle(writeFile);
}


