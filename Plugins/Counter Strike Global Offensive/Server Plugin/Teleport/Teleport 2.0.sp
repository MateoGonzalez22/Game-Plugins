#include <sourcemod>
#include <sdktools>

#pragma semicolon 1
#pragma newdecls required


/* A MEJORAR:

	- Eliminar todos los comandos y utilizar uno solo (tp), implementando argumentos en el comando
	- Inicializar el Array de Posiciones fuera de los metodos cuando inicia cada mapa
	- Guardar en el Array siempre que se agregue una nueva posicion (para utilizar el array como "archivo" para no tener que leer siempre el cfg)
	- Sobreescribir el archivo cada vez que el Array se modifica

*/

public Plugin myinfo = 
{
	name = "Teleport",
	author = "tute",
	description = "Teleport",
	version = "2.0",
	url = ""
};

public void OnPluginStart()
{	
	
	RegConsoleCmd("getpos", Command_GetPos, "");
	RegConsoleCmd("savepos", Command_SavePos, "");
	RegConsoleCmd("tp", Command_Teleport, "");
	RegConsoleCmd("list", Command_List, "");
	RegConsoleCmd("delete", Command_Delete, "");
	
	RegConsoleCmd("test", test, "");
	createFiles();
	PrintToServer("Plugin Teleport loaded!");
	
}

public Action test(int client, int args){

	

}

//----------------------C O M A N D O S------------------------------ 

public Action Command_Teleport(int client, int args){
	
	if (args == 1){
		
		char lugar[32];
		char arg1[32];
		char mapa[32];
		char texto[64];
		GetCmdArg(1, arg1, sizeof(arg1));
		GetCurrentMap(mapa, sizeof(mapa));
		
		if (!placeExists(mapa, arg1)){
			PrintToChat(client, " \x02[Server]\x01 La posición \x04%s\x01 no existe!", arg1);
			return Plugin_Handled;
		}
		
		ArrayList arr = returnArrayOfPositions(mapa);
		int lineas = GetArraySize(arr);
		
		
		
		for(int i = 0; i < lineas; i++){
			
			GetArrayString(arr, i, texto, sizeof(texto));
			
			SplitString(texto, " ", lugar, sizeof(lugar));
			
			
			if(StrEqual(lugar, arg1)){
				
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
				ReplyToCommand(client, " \x02[Server]\x01 Teletransportado a \x04%s", arg1);
				
				return Plugin_Handled;
			}
			
		}
		
	}
	
	return Plugin_Handled;
	
}

public Action Command_List(int client, int args){
	
	
	char texto[64];
	char mapa[32];
	
	GetCurrentMap(mapa, sizeof(mapa));
	
	ArrayList arr = returnArrayOfPositions(mapa);
	
	int lineas = GetArraySize(arr);
	
	char nombre[32];
	char lugar[32];
	char lugares[2000];
	
	ReplyToCommand(client, "Posiciones Guardadas: \n");
	
	for(int i = 0; i < lineas; i++){
		
		GetArrayString(arr, i, texto, sizeof(texto));
		SplitString(texto, " ", nombre, sizeof(nombre));
		Format(lugar, sizeof(lugar), "%s - ", nombre);
		StrCat(lugares, sizeof(lugares), lugar);
		
	}
	ReplyToCommand(client, lugares);
	
	return Plugin_Handled;
}

public Action Command_GetPos(int client, int args){
	
	float pos[3];
	
	GetClientAbsOrigin(client, pos);
	
	
	PrintToChatAll("Posición: %f %f %f", pos[0], pos[1], pos[2]);
	
	return Plugin_Handled;
	
}

public Action Command_SavePos(int client, int args){
	
	
	if (args == 1){
		
		char arg1[32];
		GetCmdArg(1, arg1, sizeof(arg1));
		
		char mapa[32];
		
		GetCurrentMap(mapa, sizeof(mapa));
		
		if(!placeExists(mapa, arg1)){
			savePosition(client, arg1, mapa);
			ReplyToCommand(client, " \x02[Server]\x01 Posición \x04%s \x01guardada!", arg1);
		}else{
			ReplyToCommand(client, " \x02[Server]\x01 La posición \x04%s \x01ya existe!", arg1);
		}
		
	}
	
	return Plugin_Handled;
}

public Action Command_Delete(int client, int args){
	
	if (args == 1){
		
		char mapa[32];
		char arg1[32];
		GetCmdArg(1, arg1, sizeof(arg1));
		GetCurrentMap(mapa, sizeof(mapa));
		
		
		if(placeExists(mapa, arg1)){
		
			GetCurrentMap(mapa, sizeof(mapa));
			PrintToChat(client, " \x02[Server]\x01 Posición \x04%s \x01eliminada!", arg1);
			deletePlace(mapa, arg1);
			
		}
		else {
			
			PrintToChatAll(" \x02[Server]\x01 La posición \x04%s \x01no existe!", arg1);
		}
		
	}
	return Plugin_Handled;
}

//----------------------M E T O D O S------------------------------ 

// BORRAR LUGAR
public void deletePlace(char[] mapa, char[] arg){
	
	ArrayList arr = returnArrayOfPositions(mapa);
	
	char path[64];
	char lugar[32];
	char texto[64];
	Format(path, sizeof(path), "addons/sourcemod/data/Teleport/%s.cfg", mapa);
	
	Handle overWriteFile = OpenFile(path, "w", false, NULL_STRING);
	WriteFileString(overWriteFile, "", false);
	CloseHandle(overWriteFile);
	
	Handle writeFile = OpenFile(path, "a", false, NULL_STRING);
	
	
	int lineas = GetArraySize(arr);
	
	for(int i = 0; i<lineas; i++){
		
		
		GetArrayString(arr, i, texto, sizeof(texto));
		SplitString(texto, " ", lugar, sizeof(lugar));
		
		if(!StrEqual(arg, lugar)){
			
			WriteFileString(writeFile, texto, false);
			
		}
		
	}
	CloseHandle(writeFile);
	
}

// CHECKEO SI EL LUGAR EXISTE
public bool placeExists(char[] mapa, char[] arg){
	
	ArrayList arr = returnArrayOfPositions(mapa);
	int lineas = GetArraySize(arr);
	char texto[64];
	char lugar[32];
	bool boolean = false;
	
	for(int i = 0; i < lineas; i++){
		
		GetArrayString(arr, i, texto, sizeof(texto));
		SplitString(texto, " ", lugar, sizeof(lugar));
		
		if(StrEqual(arg, lugar)){
			boolean = true;
		}		
	}	
	return boolean;
}

// GUARDO POSICION
public void savePosition(int cliente, char[] arg, char[] mapa){
	
	float position[3];
	char texto[64];
	char path[64];
	GetClientAbsOrigin(cliente, position);
	
	Format(texto, sizeof(texto), "%s = %f %f %f\n", arg, position[0], position[1], position[2]);
	Format(path, sizeof(path), "addons/sourcemod/data/Teleport/%s.cfg", mapa);
	
	Handle writeFile = OpenFile(path, "a", false, NULL_STRING);
	
	WriteFileString(writeFile, texto, false);
	
	CloseHandle(writeFile);
}

// ARRAYLIST DE TODOS LOS LUGARES DEL ARCHIVO
public ArrayList returnArrayOfPositions(char[] mapa){
	
	ArrayList arr = CreateArray(64, 0);
	
	char path[64];
	Format(path, sizeof(path), "addons/sourcemod/data/Teleport/%s.cfg", mapa);
	
	Handle file = OpenFile(path, "r", false, NULL_STRING);
	
	int lineas = countFileLines(file);
	
	
	file = OpenFile(path, "r", false, NULL_STRING);
	
	int i = 0;
	char texto[64];
	
	while(i<lineas){
		
		ReadFileLine(file, texto, sizeof(texto));
		
		PushArrayString(arr, texto);
		i++;
	}
	
	CloseHandle(file);
	return arr;
	
}

// CUENTO LINEAS EN ARCHIVO
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

// CREO ARCHIVOS
public void createFiles(){
	

	CreateDirectory("addons/sourcemod/data/Teleport", 3);

	Handle mirage = OpenFile("addons/sourcemod/data/Teleport/de_mirage.cfg", "a", false, NULL_STRING);
	CloseHandle(mirage);
	
	Handle inferno = OpenFile("addons/sourcemod/data/Teleport/de_inferno.cfg", "a", false, NULL_STRING);
	CloseHandle(inferno);
	
	Handle overpass = OpenFile("addons/sourcemod/data/Teleport/de_overpass.cfg", "a", false, NULL_STRING);
	CloseHandle(overpass);
	
	Handle dust2 = OpenFile("addons/sourcemod/data/Teleport/de_dust2.cfg", "a", false, NULL_STRING);
	CloseHandle(dust2);
	
	Handle nuke = OpenFile("addons/sourcemod/data/Teleport/de_nuke.cfg", "a", false, NULL_STRING);
	CloseHandle(nuke);
	
}