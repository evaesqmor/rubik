import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class rubik extends PApplet {

// Librería que sirve para visualizar mejor en 3D la figura


PeasyCam cam;
int dim = 3; // Dimensión del cubo
Box[] cube = new Box[dim * dim * dim]; // Definir la instancia del cubo (3 * 3 * 3)

String[] allMoves = {"f", "b", "u", "d", "l", "r"};
String sequence = "";
int counter = 0;

boolean started = false;

public void setup(){
  
 cam = new PeasyCam(this, 400); 
 int index = 0; // Índice de las piezas del cubo 
 // Ahora creamos las piezas del cubo con sus 3 dimensiones
 for(int x = -1; x<= 1 ; x++){
   for(int y = -1; y<=1; y++){
     for(int z = -1; z<=1; z++){   
       PMatrix3D matrix = new PMatrix3D();
       matrix.translate(x,y,z);//Cada una de las piezas tendrá su posición (x,y,z)
       cube[index] = new Box(matrix, x, y, z); // Para cada una de las piezas, su posición y longitud del lado
       index++;  
     }
   }
 }
 // cube[0].c = color(255, 0, 0);
 // cube[2].c = color(0, 0, 255);
 
 for (int i = 0; i < 200; i++) {
    int r = PApplet.parseInt(random(allMoves.length));
    if (random(1) < 0.5f) {
      sequence += allMoves[r];
    } else {
      sequence += allMoves[r].toUpperCase();
    }
  }

  for (int i = sequence.length()-1; i >= 0; i--) {
    String nextMove = flipCase(sequence.charAt(i));
    sequence += nextMove;
  }
}

public String flipCase(char c) {
  String s = "" + c;
  if (s.equals(s.toLowerCase())) {
    return s.toUpperCase();
  } else {
    return s.toLowerCase();
  }
}

// Función de rotación del eje Z
public void turnZ(int index, int dir) {
  for (int i = 0; i < cube.length; i++) {
    Box qb = cube[i]; // Para cada una de las piezas define un cubo
    if (qb.z == index) {
      PMatrix2D matrix = new PMatrix2D(); 
      matrix.rotate(dir * HALF_PI); // Estamos haciendo una rotación de 90 grados para la cara Z
      matrix.translate(qb.x, qb.y); // Movimiento de x e y con z fijo 
      qb.update(round(matrix.m02), round(matrix.m12), round(qb.z));
      qb.turnFacesZ(dir);
    }
  }
}

// Función de rotación del eje y
public void turnY(int index, int dir) {
  for (int i = 0; i < cube.length; i++) {
    Box qb = cube[i]; // Para cada una de las piezas define un cubo
    if (qb.y == index) {
      PMatrix2D matrix = new PMatrix2D(); 
      matrix.rotate(dir * HALF_PI); // Estamos haciendo una rotación de 90 grados para la cara Z
      matrix.translate(qb.x, qb.z); // Movimiento de x,z con y fijo
      qb.update(round(matrix.m02), qb.y, round(matrix.m12));
      qb.turnFacesY(dir);
    }
  }
}

// Función de rotación del eje x
public void turnX(int index, int dir) {
  for (int i = 0; i < cube.length; i++) {
    Box qb = cube[i]; // Para cada una de las piezas define un cubo
    if (qb.x == index) {
      PMatrix2D matrix = new PMatrix2D(); 
      matrix.rotate(dir * HALF_PI); // Estamos haciendo una rotación de 90 grados para la cara Z
      matrix.translate(qb.y, qb.z); // Movimiento de y,z con x fijo
      qb.update(qb.x, round(matrix.m02), round(matrix.m12));
      qb.turnFacesX(dir);
    }
  }
}

public void draw(){
 
  background(51); 
  if (started) {
    if (frameCount % 1 == 0) {
      if (counter < sequence.length()) {
        char move = sequence.charAt(counter);
        applyMove(move);
        counter++;
      }
    }
  }
  scale(50);
  // Representamos cada una de las piezas
  for (int i = 0; i < cube.length; i++) {
    cube[i].show();
  }
}
class Box {
  PMatrix3D matrix; //Matriz de posiciones del cubo
  int x, y , z = 0;
  boolean highlight;
  int c;
  Face[] faces = new Face[6];
  
  // Constructor
  Box(PMatrix3D m,  int x , int y, int z){
    this.matrix = m;
    this.x = x;
    this.y = y;
    this.z = z;
    c = color(255);
    //Caras del cubo
    faces[0] = new Face(new PVector(0, 0, -1), color(0, 0, 255));
    faces[1] = new Face(new PVector(0, 0, 1), color(0, 255, 0));
    faces[2] = new Face(new PVector(0, 1, 0), color(255, 255, 255));
    faces[3] = new Face(new PVector(0, -1, 0), color(255, 255, 0));
    faces[4] = new Face(new PVector(1, 0, 0), color(255, 150, 0));
    faces[5] = new Face(new PVector(-1, 0, 0), color(255, 0, 0));
  }
  
  // Girar las caras
  public void turnFacesZ(int dir){
    for(Face f: faces){
      f.turnZ(dir * HALF_PI);
    }
  }
  
  public void turnFacesY(int dir) {
    for (Face f : faces) {
      f.turnY(dir * HALF_PI); 
    }
  }
  
  public void turnFacesX(int dir) {
    for (Face f : faces) {
      f.turnX(dir * HALF_PI); 
    }
  }
  
  // Actualizando los valores de x,y,z
  public void update(int x, int y, int z){
   matrix.reset();
   matrix.translate(x,y,z);
   this.x = x;
   this.y = y;
   this.z = z; 
  }
  
  // Función para mostrar el cubo
  public void show(){
    //fill(c);
    noFill();
    stroke(0);
    strokeWeight(0.1f);
    pushMatrix(); // Guardamos la información del estado del cubo
    applyMatrix(matrix); // Cada pieza tiene una matriz
    box(1);
    for(Face f:faces){
      f.show();
    }
    popMatrix(); //Recuperamos la información
   }
  
  

}
public void keyPressed() {
  if (key == ' ') {
    started = true; 
  }
  // applyMove(key); 
}

public void applyMove(char move) {
  switch (move) {
  case 'f': 
    turnZ(1, 1);
    break;
  case 'F': 
    turnZ(1, -1);
    break;  
  case 'b': 
    turnZ(-1, 1);
    break;
  case 'B': 
    turnZ(-1, -1);
    break;
  case 'u': 
    turnY(1, 1);
    break;
  case 'U': 
    turnY(1, -1);
    break;
  case 'd': 
    turnY(-1, 1);
    break;
  case 'D': 
    turnY(-1, -1);
    break;
  case 'l': 
    turnX(-1, 1);
    break;
  case 'L': 
    turnX(-1, -1);
    break;
  case 'r': 
    turnX(1, 1);
    break;
  case 'R': 
    turnX(1, -1);
    break;
  }
}
class Face{
  PVector normal; //Vector normal de la cara seleccionada
  int c;
  
  Face(PVector normal, int c){
    this.normal = normal;
    this.c = c;
  }
  
  // También es necesaria la rotación de los vetores normales, actualizando la posición
  public void turnZ(float angle){
    PVector v2 = new PVector();
    v2.x = round(normal.x * cos(angle) - normal.y * sin(angle));
    v2.y = round(normal.x * sin(angle) + normal.y * cos(angle));
    v2.z = round(normal.z);
    normal = v2;
  }
  
  public void turnY(float angle){
    PVector v2 = new PVector();
    v2.x = round(normal.x * cos(angle) - normal.z * sin(angle));
    v2.z = round(normal.x * sin(angle) + normal.z * cos(angle));
    v2.y = round(normal.y);
    normal = v2;
  }
  
  public void turnX(float angle){
    PVector v2 = new PVector();
    v2.y = round(normal.y * cos(angle) - normal.z * sin(angle));
    v2.z = round(normal.y * sin(angle) + normal.z * cos(angle));
    v2.x = round(normal.x);
    normal = v2;
  }
  

  // Mostrar el color en la cara
  public void show() {
    pushMatrix();
    fill(c);
    noStroke();
    rectMode(CENTER);
    translate(0.5f*normal.x, 0.5f*normal.y, 0.5f*normal.z);
    if (abs(normal.x) > 0) {
      rotateY(HALF_PI);
    } else if (abs(normal.y) > 0) {
      rotateX(HALF_PI);
    }
    square(0, 0, 1);
    popMatrix();
  }
}
  public void settings() {  size(600,600, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "rubik" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
