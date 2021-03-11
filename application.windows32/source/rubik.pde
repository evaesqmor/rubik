// Librería que sirve para visualizar mejor en 3D la figura
import peasy.*;

PeasyCam cam;
int dim = 3; // Dimensión del cubo
Box[] cube = new Box[dim * dim * dim]; // Definir la instancia del cubo (3 * 3 * 3)

String[] allMoves = {"f", "b", "u", "d", "l", "r"};
String sequence = "";
int counter = 0;

boolean started = false;

void setup(){
 size(600,600, P3D); 
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
    int r = int(random(allMoves.length));
    if (random(1) < 0.5) {
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

String flipCase(char c) {
  String s = "" + c;
  if (s.equals(s.toLowerCase())) {
    return s.toUpperCase();
  } else {
    return s.toLowerCase();
  }
}

// Función de rotación del eje Z
void turnZ(int index, int dir) {
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
void turnY(int index, int dir) {
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
void turnX(int index, int dir) {
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

void draw(){
 
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
