import pygame as pg
import math
from settings import *
import sprites

class Grid():
    def __init__(self, game):
        self.tiles = [[0 for x in range(math.floor(MAX_GRID_X))] for y in range(math.floor(MAX_GRID_Y))]
        for x in range(math.floor(MAX_GRID_X)):
            for y in range(math.floor(MAX_GRID_Y)):
                self.tiles[x][y] = Tile(game, x, y)

    def getTilesInRange(self, range, rootTile, method):
        inRange = []
        for row in self.tiles:
            for tile in row:
                if method == 0:
                    distance = tile.getH(rootTile.gridX, rootTile.gridY)
                else:
                    distance = tile.getEuclidean(rootTile.gridX, rootTile.gridY)

                if(distance <= range and distance != 0):
                    inRange.append(tile)
        return inRange

    def calculateAllHeuristics(self):
        for row in self.tiles:
            for tile in row:
                tile.calculateHeuristics()

    def getTile(self, x, y):
        gridX = math.floor(((x - GRID_START_X)/TILESIZE))
        gridY = math.floor(((y - GRID_START_Y)/TILESIZE))
        if(gridX >= 0) and (gridX < MAX_GRID_X) and (gridY >= 0) and (gridY < MAX_GRID_Y):
            return self.tiles[gridX][gridY]
        else:
            return None

    def getGridTile(self, x, y):
        if (x < 0) or (x >= MAX_GRID_X) or (y < 0) or (y >= MAX_GRID_Y):
            return None
        else:
            return self.tiles[x][y]

class Tile():
    def __init__(self, game, gridX, gridY):
        self.gridX = gridX
        self.gridY = gridY
        self.game = game
        self.isOccupied = False
        self.isPassable = True
        self.entityList = []
        self.secondHighlight = None
        self.highlight = None
        self.difficulty = 0
        self.x = GRID_START_X + (TILESIZE * gridX)
        self.y = GRID_START_Y + (TILESIZE * gridY)
    def getGridPosition(self):
        return (self.gridX, self.gridY)
    def setItem(self, item):
        self.item = item
        self.item.setPosition(self.x, self.y)
        self.isOccupied = True
        self.isPassable = item.isPassable
    def setHighlight(self, highlight):
        self.highlight = highlight
        self.highlight.setPosition(self.x, self.y)
    def setSecondHighlight(self, highlight):
        self.secondHighlight = highlight
        self.secondHighlight.setPosition(self.x, self.y)
    def destroyItem(self):
        self.isOccupied = False
        self.isPassable = True
        self.item.kill()
        self.game.item_sprites.remove(self.item)
    def destroyHighlight(self):
        if(self.highlight is not None):
            self.highlight.kill()
            self.game.highlight_sprites.remove(self.highlight)
    def destroySecondHighlight(self):
        if(self.secondHighlight is not None):
            self.secondHighlight.kill()
            self.secondHighlight = None
            self.game.highlight_sprites.remove(self.secondHighlight)

    def calculateHeuristics(self):
        self.heuristics = [[0 for x in range(math.floor(MAX_GRID_X))] for y in range(math.floor(MAX_GRID_Y))]
        self.euclidean = [[0 for x in range(math.floor(MAX_GRID_X))] for y in range(math.floor(MAX_GRID_Y))]
        for row in self.game.grid.tiles:
            for goal in row:
                self.euclidean[goal.gridX][goal.gridY] = math.sqrt(math.pow((self.gridX - goal.gridX), 2) + math.pow((self.gridY - goal.gridY), 2))
                self.heuristics[goal.gridX][goal.gridY] = abs(self.gridX - goal.gridX) + abs(self.gridY - goal.gridY)
    def getH(self, x, y):
        return self.heuristics[x][y]

    def getEuclidean(self, x, y):
        return self.euclidean[x][y]

    def isMouseOver(self, x, y):
        if(x > self.x) and (x < (self.x + TILESIZE)) and (y > self.y) and (y < (self.y + TILESIZE)):
            return True
        else:
            return False

class Shooter():
    def __init__(self, game, entity, color, start, vector, magnitude, damage):
        self.entity = entity
        self.color = color
        self.start = start
        self.game = game
        self.vector = vector
        self.magnitude = magnitude
        self.damage = damage
        self.hasShot = False
        self.hasCollided = False
        self.bullet = None
    def shoot(self):
        self.hasShot = True
        self.bullet = sprites.Bullet(self.game, self, self.entity, self.color, self.start, self.vector, self.magnitude, self.damage)

class Node():
    def __init__(self, tile, goal, path = [], cost = 0):
        self.tile = tile
        self.path = path
        self.cost = cost
        self.goal = goal
    def setPath(self, path):
        self.path = path

    def setPath(self, cost):
        self.cost = cost

    def addToPath(self, node):
        self.path.append(node)

    def __lt__(self, other):
        return self.tile.getH(self.goal.gridX, self.goal.gridY) < other.tile.getH(self.goal.gridX, self.goal.gridY)

    def __eq__(self, other):
            return self.tile.getH(self.goal.gridX, self.goal.gridY) == other.tile.getH(self.goal.gridX, self.goal.gridY)
