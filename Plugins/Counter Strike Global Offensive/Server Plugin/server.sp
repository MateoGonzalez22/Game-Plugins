#include <sourcemod>
#include <sdktools>

#pragma semicolon 1
#pragma newdecls required


public Plugin myinfo = 
{
	name = "Server",
	author = "tute",
	description = "Manage commands",
	version = "1.0",
	url = ""
};

public void OnPluginStart()
{
	RegConsoleCmd("server", Command_Server, "");
	
}
public Action Command_Server(int client, int args){
	
	if(GetUserAdmin(client) == INVALID_ADMIN_ID){
		ReplyToCommand(client, "[SM] No tenes acceso a este comando");
		return Plugin_Handled;
	}
	
	else if (args == 1){
  	
	    char arg1[32];
	    GetCmdArg(1, arg1, sizeof(arg1));
	    
	    if(StrEqual(arg1, "rs")){
	   		
   			PrintToChatAll(" \x01[Server]\x04 Reiniciando!");
   			ServerCommand("mp_restartgame 1");
   			
	    }
	    
	    else if(StrEqual(arg1, "practica")){
	    	
	    	PrintToChatAll(" \x01[Server]\x04 Modo Practica Activado");
	    	ServerCommand("exec practica1");
	    	
	   	}
	   	
	   	else if(StrEqual(arg1, "competitive")){
	    	
	    	PrintToChatAll(" \x01[Server]\x04 Modo Competitivo Activado");
	    	ServerCommand("exec gamemode_competitive");
	    	
	   	}
	   	
	   	else if(StrEqual(arg1, "casual")){
	    	
	    	PrintToChatAll(" \x01[Server]\x04 Modo Casual Activado");
	    	ServerCommand("exec gamemode_casual");
	    	
	   	}
	   	
	   	else if(StrEqual(arg1, "armas")){
			
			listArmas(client);
	   		
   		}
	   	else{
	   		
	   		PrintToChat(client, " \x01[Server]\x02 No se reconoce el comando");
	   		PrintToChat(client, "");
	   	}
	    return Plugin_Handled;
	}
	
	else if(args == 2){

        char arg1[32];
        GetCmdArg(1, arg1, sizeof(arg1));
        char arg2[32];
        GetCmdArg(2, arg2, sizeof(arg2));
        
        if(StrEqual(arg1, "rondas")){
        	
        	int rondas = StringToInt(arg2);
        	ServerCommand("mp_maxrounds %d", rondas);
         
        }
        else{
            usage(client);
        }
        return Plugin_Handled;
       
	}
      
	else if(args == 3){

        char arg1[32];
        GetCmdArg(1, arg1, sizeof(arg1));
        char arg2[32];
        GetCmdArg(2, arg2, sizeof(arg2));
        char arg3[32];
        GetCmdArg(3, arg3, sizeof(arg3));
        
        char tDefaultPrimary[60] = "mp_t_default_primary weapon_";
        char ctDefaultPrimary[60] = "mp_ct_default_primary weapon_";
        char tDefaultSecondary[60] = "mp_t_default_secondary weapon_";
        char ctDefaultSecondary[60] = "mp_ct_default_secondary weapon_";
        
        
        
        
        
        if(StrEqual(arg1, "all") && StrEqual(arg2, "primary")){
        	
        	StrCat(tDefaultPrimary, sizeof(tDefaultPrimary), arg3);
        	StrCat(ctDefaultPrimary, sizeof(ctDefaultPrimary), arg3);
        	
        	PrintToChatAll(" \x01[Server]\x04 Arma primaria por defecto: %s", arg3);
        	
        	ServerCommand(tDefaultPrimary);
        	ServerCommand(ctDefaultPrimary);
        }
        	
        else if(StrEqual(arg1, "all") && StrEqual(arg2, "secondary")){

        	StrCat(tDefaultSecondary, sizeof(tDefaultSecondary), arg3);
        	StrCat(ctDefaultSecondary, sizeof(ctDefaultSecondary), arg3);
        	
    		ServerCommand(tDefaultSecondary);
        	ServerCommand(ctDefaultSecondary);
        	
        	PrintToChatAll(" \x01[Server]\x04 Arma secundaria por defecto: %s", arg3);
        	

				
		}
		
		else if(StrEqual(arg1, "t") && StrEqual(arg2, "primary")){
		
			StrCat(tDefaultPrimary, sizeof(tDefaultPrimary), arg3);
			
			ServerCommand(tDefaultPrimary);
			
			PrintToChatAll(" \x01[Server]\x04 Arma primaria por defecto para terroristas: %s", arg3);
			
		}
		
		else if(StrEqual(arg1, "t") && StrEqual(arg2, "secondary")){
		
			StrCat(tDefaultSecondary, sizeof(tDefaultSecondary), arg3);
			ServerCommand(tDefaultSecondary);
			
			PrintToChatAll(" \x01[Server]\x04 Arma secundaria por defecto para terroristas: %s", arg3);
			
		}
		
		else if(StrEqual(arg1, "ct") && StrEqual(arg2, "secondary")){
		
			StrCat(ctDefaultSecondary, sizeof(ctDefaultSecondary), arg3);
			ServerCommand(ctDefaultSecondary);
			
			PrintToChatAll(" \x01[Server]\x04 Arma secundaria por defecto para anti-terroristas: %s", arg3);	
		
			
		}	
		
		else if(StrEqual(arg1, "ct") && StrEqual(arg2, "primary")){
		
			StrCat(ctDefaultPrimary, sizeof(ctDefaultPrimary), arg3);
			
			ServerCommand(ctDefaultPrimary);
			
			PrintToChatAll(" \x01[Server]\x04 Arma primaria por defecto para anti-terroristas: %s", arg3);
			
		}
		else{
			usage(client);
		}
       		
	}
	else{
		usage(client);
	}
	
	return Plugin_Handled;
}
	

public void usage(int client){

	PrintToChat(client, "[Server]\x04 Uso:");
	PrintToChat(client, "rs : Reiniciar servidor");
	PrintToChat(client, "practica : Activar Modo Practica");
	PrintToChat(client, "competitive: Modo Competitivo Activado");
	PrintToChat(client, "all primary <arma>: Setear arma primaria para todos");
	PrintToChat(client, "all secondary <arma>: Setear arma secundaria para todos");
	PrintToChat(client, "t primary <arma>: Setear arma primaria para TT");
	PrintToChat(client, "ct primary <arma>: Setear arma primaria para CT");
	PrintToChat(client, "t secondary <arma>: Setear arma secundaria para TT");
	PrintToChat(client, "ct secondary <arma>: Setear arma secundaria para CT");
	PrintToChat(client, "rondas numero: Setear cantidad de rondas");
}


public void listArmas(int client){
	
	ReplyToCommand(client, "[Server] Guns: usp_silencer, glock, p250, deagle, hkp2000, m4a1_silencer, m4a1, galilar, ssg08, sg556, awp, famas");
	
}


