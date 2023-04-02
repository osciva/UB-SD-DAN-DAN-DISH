[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10150910&assignment_repo_type=AssignmentRepo)
# Software Distribuït 2023

| Professor    | Mail            | Classes                                                               |
|--------------|-----------------|-----------------------------------------------------------------------|
| Eloi Puertas | epuertas@ub.edu | Teoria, dimecres de 15 a 17h. Pràctiques, dijous de 17 a 19h (Grup B) |
| Blai Ras     | blai.ras@ub.edu | Pràctiques, dimecres de 17 a 19h (Grup F) i de 19 a 21h (Grup A)      |

## P1 - Client / Servidor

### Execució 

L'execució seguirà obligatòriament els següents passos:

**Servidor**

![image](https://user-images.githubusercontent.com/81873328/229373473-a7dac35f-b219-44c3-8e08-427999ddecb2.png)

![image](https://user-images.githubusercontent.com/81873328/229373504-20ba095b-92d7-4d27-86ad-6b3f8725c898.png)

![image](https://user-images.githubusercontent.com/81873328/229373518-bf92daa5-9c95-441a-b494-5ad31bef614a.png)

![image](https://user-images.githubusercontent.com/81873328/229373541-41b775e4-8d30-4ead-b95c-ef70e5426fdb.png)

* -p: port a on establir-se

com no hem creat el mode client vs client no es necessari especificar si -m 0 o -m 1 (mode 0 o mode 1)


**Client**

![image](https://user-images.githubusercontent.com/81873328/229373473-a7dac35f-b219-44c3-8e08-427999ddecb2.png)

![image](https://user-images.githubusercontent.com/81873328/229373504-20ba095b-92d7-4d27-86ad-6b3f8725c898.png)

![image](https://user-images.githubusercontent.com/81873328/229373518-bf92daa5-9c95-441a-b494-5ad31bef614a.png)

![image](https://user-images.githubusercontent.com/81873328/229373668-b2dfe43b-7564-4310-8873-f48251261341.png)

on localhost pot canviar-se per l'IP de la màquina a on connectar-se
* -h: IP o nom de la màquina a on connectar-se
* -p: port on trobar el servidor

Podeu executar-ho usant la consola, o podeu editar les _run configurations_ del projecte tal i com es mostra a continuació:

![image](https://user-images.githubusercontent.com/81873328/229373893-b2ca0c75-e7bb-43ec-ad18-40f91a827285.png)

hauria de sortir això:

![image](https://user-images.githubusercontent.com/81873328/229373943-ef17f582-8af1-43fb-87ba-31d64f3a86eb.png)

i després...

![image](https://user-images.githubusercontent.com/81873328/229373958-32ecfc6e-64a8-4fbf-a887-02d121785a51.png)

hauria de sortir això:

![image](https://user-images.githubusercontent.com/81873328/229374003-e0402ace-5447-401b-adda-b98bc3917657.png)


#### FEEDBACK RECOLLIT AL FER TESTING COM A CLIENTS

SERVER|CLIENT|RESULTAT|
------|------|------|
BLAI | A09|  Cannot invoke "String.hashCode()" because "<local2>" is null|
A02  | A09|   no reben el hello|
A03  | A09|   no em puc connectar (no troba la ip? (els hi passa amb uns quants, així que no és un problema del nostre codi ))|
A02  | A09|   2n intent, un cop arreglat el problema anterior, tornem a provar i ens podem connectar al seu server i enviar strings, però ens hem adonat d'un error fatal que teníem, tenim la trama CHARGE i no CHARG per la qual tenim diferent protocol i ens ha originat un problema-> Ho acabem solucionat|
A02  | A09|   3r intent, hem tornat a iniciar la connexió i per la nostra part sembla que funciona (s'envia bé l'acció) però ells llegeixen malament l'acció i ens retorna error|
A02  | A09|   l'action ens dona problemes no els arriba l'acció -> enviavem un 4 de més -> Ho acabem solucionant|
A02  | A09|   4t intent: l'action ens torna a donar problemes -> reben algo desconegut -> Ho acabem solucionant|
A02  | A09|   5é intent, el nostre mètode escriureString tenia un 4 innecessari|
A02  | A09|   Tenim error al receiveadmit: en comptes de llegir el bool de 0 o 1 nosaltres tenim un int per tant cridem a llegir Int i no a llegir Byte ---> solucionat|
A02  | A09|   El sendAction dona error perquè escribim un int innecessari-> solucionat|

Vam repetir amb el grup A02 tants cops perquè a ells els funcionava bé fins la part d'action i els errors que rebiem nosaltres eren anteriors a  l'action, per tant eren una font fiable de testeig. I més quan
a la que van arreglar aquell problema van poder jugar partides completes sense problemes.


##### FEEDBACK RECOLLIT AL FER TESTING COM A CLIENTS 

Com a Server nosaltres teníem problemes amb la resta, ja que al començament ningú es podia connectar i encara avui, no sabem perquè, així que vam canviar d'ordinador
2 cops fins que vam aconseguir que anés. Després es va connectar el grup A02 i el client es podia connectar però es quedava aturat perquè no responíem al seu hello. També ho va provar l'A03, però passava el mateix. Així que vam estar una bona estona arreglant aquest error i durant aquell temps molts
altres grups es van intentar connectar però sense éxit tampoc. Després ens fallava el sendAdmit pel problema mencionat anteriorment on en byte d'admit que podia ser 0 o 1 nosaltres
el consideràvem un int i enviavem un int (cosa que ningú esperava) perquè al protocol deia que havia de ser un byte.
Quan vam arreglar aquests errors vam tornar a provar amb altres grups com l'A08, però no es podien connectar.



Vam poder completar el projecte fora de la classe de testing. La setmana següent, amb l'ajuda d'altres grups que ens van permetre runnejar, vam poder arreglar els errors resultants i provar amb 3 grups diferents si funcionava.

  
###### AUTORS
  
  -> IVAN MANSILLA FLORES 
  
  -> OSCAR DE CARALT ROY 
