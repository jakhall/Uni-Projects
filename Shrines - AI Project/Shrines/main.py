import pygame as pg
import random
import queue
import time
from sprites import *
from settings import *
from environment import *
from widgets import *


class Game:
    #Basic Functions

    def __init__(self):
        pg.init();
        pg.mixer.init();
        self.screen = pg.display.set_mode((WINDOW_WIDTH, WINDOW_HEIGHT))
        pg.display.set_caption(TITLE)
        self.clock = pg.time.Clock()
        self.running = False
        self.inMenu = True
        self.mode = -1
        self.playersTurn = True
        self.item_sprites = pg.sprite.Group()
        self.highlight_sprites = pg.sprite.Group()
        self.entity_sprites = pg.sprite.Group()
        self.widget_sprites = pg.sprite.Group()
        self.widgets = []
        self.items = []

    def new(self):
        self.playerTeam = 1
        self.enemyTeam = 2
        self.currentTeam = 2
        self.enemyMoves = []
        self.enemyCaptures = []
        self.goalShrines = []
        self.isEnemyCapturing = False
        self.isEnemyMoving = False
        self.shrinePlaced = False
        self.isEnemyShooting = False
        self.enemyShots = []
        self.projectile_sprites = pg.sprite.Group()
        self.grid = Grid(self)
        self.grid.calculateAllHeuristics()
        self.entityList = []
        self.entityList.append(Entity(self, 3, 20, 1, 2))
        self.entityList.append(Entity(self, 11, 19, 1, 4))
        self.entityList.append(Entity(self, 18, 20, 1, 2))
        self.entityList.append(Entity(self, 3, 1, 2, 2))
        self.entityList.append(Entity(self, 11, 2, 2, 4))
        self.entityList.append(Entity(self, 18, 1, 2, 2))
        self.selectedEntity = None
        self.selectedItem = None
        self.phase = 1
        self.phaseLabel = Label(self, "Phase: Moving", 36, GRID_END_X, GRID_END_Y)
        self.addButtons()


    def addButtons(self):
        self.buttons = []
        if(self.mode == 1):
            self.buttons.append(Button(self, "Attack", 2, GRID_START_X, GRID_END_Y + BUTTON_HEIGHT))
            #self.buttons.append(Button(self, "Draw", 0, GRID_START_X, GRID_END_Y + BUTTON_HEIGHT))
        self.buttons.append(Button(self, "END TURN", 3,  GRID_START_X, GRID_END_Y + BUTTON_HEIGHT*2 + 10))


    def run(self):
        self.playing = True
        while self.playing:
            self.clock.tick(FPS)
            self.events()
            self.update()
            self.draw()

    def update(self):
        self.item_sprites.update()
        self.projectile_sprites.update()
        self.highlight_sprites.update()
        self.entity_sprites.update()
        self.widget_sprites.update()



        if self.isEnemyCapturing:
            if len(self.enemyCaptures) > 0:
                if(self.enemyCaptures[0][0].owner == self.enemyCaptures[0][1].team):
                    self.enemyCaptures.remove(self.enemyCaptures[0])
                elif(not self.enemyCaptures[0][0].beingCaptured):
                    self.enemyCaptures[0][0].capture(self.enemyCaptures[0][1])
            else:
                self.isEnemyCapturing = False
                self.enemyMoving()


        if self.isEnemyMoving:
            #print("Moving: " + str(self.currentTeam))
            if len(self.enemyMoves) > 0:
                if(self.enemyMoves[0][0].hasMoved):
                    self.enemyMoves.remove(self.enemyMoves[0])
                elif(not self.enemyMoves[0][0].moveActivated):
                    self.enemyEntityMove(self.enemyMoves[0][0], self.enemyMoves[0][1])
            else:
                self.isEnemyMoving = False
                self.enemyAttacking()

        if self.isEnemyShooting:
            if len(self.enemyShots) > 0:
                if(self.enemyShots[0].hasCollided):
                    self.enemyShots.remove(self.enemyShots[0])
                elif(not self.enemyShots[0].hasShot):
                    self.enemyShots[0].shoot()
            else:
                self.isEnemyShooting = False
                self.nextTurn()

        if(self.shrinePlaced == True):
            playerCheck = False
            shrineCheck = False
            player = self.entityList[0]
            for entity in self.entityList:
                if entity.team != player.team:
                    playerCheck = True
            if(self.items[0].owner != 0):
                shrine = self.items[0]
                for item in self.items:
                    if(item.owner != shrine.owner):
                        shrineCheck = True
            else:
                shrineCheck = True
            if(shrineCheck == False or playerCheck == False):
                if(shrineCheck == False):
                    self.winner = shrine.owner
                else:
                    self.winner = player.team
                widgets = self.widgets[:]
                for widget in widgets:
                    widget.destroy()
                self.playing = False
                self.running = False

    def events(self):
        for event in pg.event.get():
            self.selectItem(event)
            if(self.playersTurn):
                self.make_item(event)
                self.updateWidgets(event)
                self.selectEntity(event)
            if event.type == pg.QUIT:
                if self.playing:
                    self.playing = False
                pg.quit()
            if(self.phase < 1):
                pass
            elif(self.phase < 2):
                self.phaseMoving(event)
            elif(self.phase < 3):
                self.phaseAttacking(event)

    def draw(self):
        self.screen.fill(LIGHTGRAY)
        self.item_sprites.draw(self.screen)
        self.projectile_sprites.draw(self.screen)
        self.entity_sprites.draw(self.screen)
        self.highlight_sprites.draw(self.screen)
        self.drawGrid()
        self.widget_sprites.draw(self.screen)
        pg.display.flip()


    #Added Functions

    def drawGrid(self):
        for x in range(GRID_START_X, GRID_END_X + TILESIZE, TILESIZE):
            pg.draw.line(self.screen, BLACK, (x, GRID_START_Y), (x, GRID_END_Y))
        for y in range(GRID_START_Y, GRID_END_Y + TILESIZE, TILESIZE):
            pg.draw.line(self.screen, BLACK, (GRID_START_X, y), (GRID_END_X, y))


    def make_item(self, event):
        if event.type == pg.KEYDOWN:
            (mouseX, mouseY) = pg.mouse.get_pos()
            tile = self.grid.getTile(mouseX, mouseY)
            if(event.key == pg.K_e):
                    if tile.isOccupied:
                        tile.destroyItem()
                    else:
                        tile.setItem(Obstacle(self, False, tile))
            if(event.key == pg.K_s):
                    if tile.isOccupied:
                        tile.destroyItem()
                        self.items.remove(tile.item)
                    else:
                        if(self.shrinePlaced == False):
                            self.shrinePlaced = True
                        tile.setItem(Shrine(self, False, tile))
                        self.items.append(tile.item)

    def selectTile(self, tile, x, y):
        if(tile != self.selectedEntity.tile) and (tile in self.selectedEntity.moveableTiles):
            menu = TileMenu(self, x, y, tile)

    def selectItem(self, event):
        if event.type == pg.MOUSEBUTTONUP:
            if event.button == 1:
                (mouseX, mouseY) = pg.mouse.get_pos()
                for item in self.items:
                    if item.isMouseOver(mouseX, mouseY):
                        if self.selectedItem != item:
                            self.deselectAll()
                            self.selectedItem = item
                            self.selectedItem.select()
                    elif self.selectedItem == item:
                        self.selectedItem.deselect()
                        self.selectedItem = None

    def deselectAll(self):
        if(self.selectedEntity is not None):
            self.selectedEntity.unselectEntity()
            self.selectedEntity = None
        if(self.selectedItem is not None):
            self.selectedItem.deselect()
            self.selectedItem = None

    def findAStarPath(self, startNode, goalNode, inclusive):
        searchQueue = queue.PriorityQueue()
        goalFound = False
        explored = []
        currentNode = Node(startNode, goalNode, [], 0)
        optimalPath = None
        searchQueue.put((0, currentNode))
        a = time.time() * 1000
        while(not searchQueue.empty()) and (not goalFound):
            (currentWeight, currentNode) = searchQueue.get()
            explored.append(currentNode.tile)
            newNeighbours = self.getAllNeighbours(currentNode, explored, goalNode, inclusive)
            for tile in newNeighbours:
                explored.append(tile)
                #tile.setItem(Highlight(self, True, LIGHTGREEN))
            for tile in newNeighbours:
                path = currentNode.path[:]
                path.append(tile)
                cost = currentNode.cost + tile.difficulty
                node = Node(tile, goalNode, path, cost)
                if self.checkGoal(node, goalNode):
                    goalFound = True
                    optimalPath = node.path
                    #for tile in node.path:
                        #tile.setHighlight(Highlight(self, BLUE))
                else:
                    weight = self.weight(node, goalNode)
                    searchQueue.put((weight, node))
        b = time.time() * 1000
        #print("Time for goal reached: " +  str(b - a))
        return optimalPath


    def selectEntity(self, event):
        if event.type == pg.MOUSEBUTTONUP:
            (mouseX, mouseY) = pg.mouse.get_pos()
            tile = self.grid.getTile(mouseX, mouseY)
            if event.button == 1:
                for entity in self.entityList:
                    if entity.isMouseOver(mouseX, mouseY):
                        if not entity.isSelected:
                            if self.selectedEntity is not None:
                                self.selectedEntity.unselectEntity()
                            self.selectedEntity = entity
                            entity.selectEntity()
                    else:
                        if entity == self.selectedEntity:
                            entity.unselectEntity()
                            self.selectedEntity = None

    def phaseMoving(self, event):
        if event.type == pg.MOUSEBUTTONUP:
            if event.button == 1:
                if(self.selectedEntity is not None and self.selectedEntity.team == self.playerTeam):
                    (mouseX, mouseY) = pg.mouse.get_pos()
                    if self.selectedEntity.isMouseOver(mouseX, mouseY):
                        self.selectedEntity.moveOptions()
            if event.button == 3:
                if(self.selectedEntity is not None  and self.selectedEntity.team == self.playerTeam):
                    (mouseX, mouseY) = pg.mouse.get_pos()
                    tile = self.grid.getTile(mouseX, mouseY)
                    self.selectTile(tile, mouseX, mouseY)
                    for item in self.items:
                        (mouseX, mouseY) = pg.mouse.get_pos()
                        if item.isMouseOver(mouseX, mouseY):
                            if isinstance(item, Shrine):
                                self.shrineMenu(item, mouseX, mouseY)


    def shrineMenu(self, shrine, x, y):
        for entity in shrine.getAllInRange():
            if(entity == self.selectedEntity):
                menu = ShrineMenu(self, x, y, shrine, self.selectedEntity)


    def phaseAttacking(self, event):
        if event.type == pg.MOUSEBUTTONUP:
            if event.button == 1:
                if(self.selectedEntity is not None and self.selectedEntity.team == self.playerTeam):
                    (mouseX, mouseY) = pg.mouse.get_pos()
                    if self.selectedEntity.isMouseOver(mouseX, mouseY):
                        self.selectedEntity.highlightAttack()
            if event.button == 3:
                if(self.selectedEntity is not None and self.selectedEntity.team == self.playerTeam):
                    (mouseX, mouseY) = pg.mouse.get_pos()
                    if(self.selectedEntity.selectedAttackRow is not None):
                        menu = AttackMenu(self, mouseX, mouseY, self.selectedEntity, self.selectedEntity.selectedAttackRow[1])

    def getAllNeighbours(self, node, explored, goal, inclusive):
        directions = [1, -1]
        neighbours = []
        for d in directions:
            tile = self.grid.getGridTile(node.tile.gridX + d, node.tile.gridY)
            if(tile != None) and (tile.isPassable) and (tile not in explored):
                neighbours.append(tile)
            elif inclusive and tile == goal:
                neighbours.append(tile)
        for d in directions:
            tile = self.grid.getGridTile(node.tile.gridX, node.tile.gridY + d)
            if(tile != None) and (tile.isPassable) and (tile not in explored):
                neighbours.append(tile)
            elif inclusive and tile == goal:
                neighbours.append(tile)

        return neighbours


    def checkGoal(self, node, goal):
        return node.tile == goal

    def weight(self, node, goal):
        return node.cost + node.tile.getH(goal.gridX, goal.gridY)

    def enemyTurn(self):
        self.enemyDrawing()

    def enemyDrawing(self):
    #    print("Enemy Drawing..")
        self.enemyCapturing()

    def getOppositeTeam(self, team):
        if(team == 1):
            return 2
        else:
            return 1

    def enemyCapturing(self):
        #print("Enemy Capturing..")
        for entity in self.entityList:
            if entity.team == self.currentTeam:
                shrines = entity.shrineDistances()
                goal = self.nearestGoal(shrines)
                if goal is not None:
                    if (entity in goal[0].getAllInRange()) and (not goal[0].occupied()) and (goal[0].owner != entity.team):
                        self.enemyCaptures.append((goal[0], entity))
                        entity.moveActivated = True
                        entity.hasMoved = True
                    elif((entity in goal[0].getAllInRange()) and (goal[0].owner == self.getOppositeTeam(entity.team))):
                        nearestEnemy = entity.nearestEnemy()
                        if(nearestEnemy is not None):
                            self.enemyMoves.append((entity, nearestEnemy))
        self.isEnemyCapturing = True

    def enemyMoving(self):
        #print("Enemy Moving..")
        for entity in self.entityList:
            check = False
            for move in self.enemyMoves:
                if entity == move[0]:
                    check = True
            if(check == False):
                if entity.team == self.currentTeam:
                    if(not entity.hasMoved):
                        self.enemyMoves.append((entity, None))
        self.isEnemyMoving = True


    def enemyEntityMove(self, entity, goal):
        if(entity.team != self.currentTeam):
            entity.moveActivated = True
            entity.hasMoved = True
        else:
            shrines = entity.shrineDistances()
            goals = []
            unselectedShrines = shrines[:]
            for shrine in shrines:
                if shrine[0] not in self.goalShrines:
                    unselectedShrines.append(shrine)
            if(len(unselectedShrines) > 0):
                shrines = unselectedShrines

            for shrine in shrines:
                if shrine[0].owner != self.currentTeam:
                    goals.append(shrine)

            if goal is None:
                goal = self.nearestGoal(goals)
                if goal is not None:
                    if goal[0] not in self.goalShrines:
                        self.goalShrines.append(goal[0])
            else:
               goal = ((goal, None))

            if goal is None:
               entity.hasMoved = True


            if not entity.hasMoved:
                if(goal is not None):
                    totalPath = self.findAStarPath(entity.tile, goal[0].tile, True)
                    availableTiles = entity.calculateOptions()
                    availablePath = []
                    if(totalPath is not None):
                        for tile in totalPath:
                            if(tile in availableTiles):
                                availablePath.append(tile)
                    entity.movePath(availablePath)

    def nearestGoal(self, goals):
        selected = None
        for goal in goals:
            if(selected is None):
                selected = goal
            else:
                if selected[1] > goal[1]:
                    selected = goal
        return selected


    def enemyAttacking(self):
        #print("Enemy Attacking..")
        for entity in self.entityList:
            attackOptions = []
            if entity.team == self.currentTeam:
                attackRows = entity.calculateAttack()
                for row in attackRows:
                    if (row[2] is not None):
                        if(row[2].team != entity.team):
                            shooter = Shooter(self, entity, BLACK, (entity.x + GRID_START_X + TILESIZE/2, entity.y + GRID_START_Y + TILESIZE/2), (row[1][0], row[1][1]), 10, 1)
                            attackOptions.append(shooter)
            if(len(attackOptions) > 0):
                self.enemyShots.append(attackOptions[0])
        self.isEnemyShooting = True

    def nextTurn(self):
        self.phase = 1
        self.goalShrines = []
        for entity in self.entityList:
            entity.moveActivated = False
            entity.endShot = False
            entity.hasMoved = False
        if(self.mode == 1):
            #self.buttons.append(Button(self, "Draw", 0, GRID_START_X, GRID_END_Y + BUTTON_HEIGHT))
            self.buttons.append(Button(self, "Attack", 2, GRID_START_X, GRID_END_Y + BUTTON_HEIGHT))


    def buttonPressed(self, button):
        #print("Pressed " + str(button.id))
        id = button.id
        if(id < 1):
            self.phase = 0
            button.destroy()
            self.buttons.append(Button(self, "Move", 1, GRID_START_X, GRID_END_Y + BUTTON_HEIGHT))
        elif(id < 2):
            self.phase = 1
            button.kill()
            self.widgets.remove(button)
            self.widget_sprites.remove(button)
            self.buttons.append(Button(self, "Attack", 2, GRID_START_X, GRID_END_Y + BUTTON_HEIGHT))
        elif(id < 3):
            self.phase = 2
            button.kill()
            self.widgets.remove(button)
            self.widget_sprites.remove(button)
        elif(id < 4):
            self.phase = 3
            if(self.mode == 0):
                if self.currentTeam == self.enemyTeam:
                    self.currentTeam = self.playerTeam
                else:
                   self.currentTeam = self.enemyTeam

            self.enemyTurn()

        elif(id < 5):
            self.btnPLayerComputer.isSelected = True
            self.btnComputerComputer.isSelected = False
            self.mode = 1
        elif(id < 6):
            self.btnPLayerComputer.isSelected = False
            self.btnComputerComputer.isSelected = True
            self.mode = 0
        elif(id < 7):
            if(self.mode >= 0 ):
                self.inMenu = False
        elif(id < 8):
            pg.quit()
            #if self.currentTeam == self.enemyTeam:
            #    self.currentTeam = self.playerTeam
            #else:
            #    self.currentTeam = self.enemyTeam


    def updateWidgets(self, event):
        widgets = self.widgets[:]
        for widget in widgets:
            if(widget in self.widgets):
                widget.events(event)

    def show_start_screen(self):
		#print("Starting shrines.. ")
		#print("Press E to create obstacle")
		#print("Press S to create shrine")
        self.screen.fill(LIGHTGRAY)
        self.position = 150
        self.btnPLayerComputer = Button(self, "Player Vs Computer", 4, WINDOW_WIDTH/2 - BUTTON_WIDTH, self.position + 100, BUTTON_WIDTH*2)
        self.btnComputerComputer = Button(self, "Computer vs Computer", 5, WINDOW_WIDTH/2 - BUTTON_WIDTH, self.position + 200, BUTTON_WIDTH*2)
        self.btnStart = Button(self, "Start", 6, WINDOW_WIDTH - BUTTON_WIDTH*1.5, self.position + 350)
        self.btnQuit = Button(self, "Quit", 7, BUTTON_WIDTH/2, self.position + 350)
        self.title = Text(self, "SHRINES", 40,  WINDOW_WIDTH/2 - BUTTON_WIDTH/1.5, self.position )
        while self.inMenu:
            self.clock.tick(FPS)
            self.screen.fill(LIGHTGRAY)
            self.widget_sprites.update()
            self.widget_sprites.draw(self.screen)
            for event in pg.event.get():
                self.updateWidgets(event)
                if event.type == pg.QUIT:
                    pg.quit()
            pg.display.flip()

        widgets = self.widgets[:]
        for widget in widgets:
            widget.destroy()

        self.running = True


    def show_go_screen(self):
        self.inMenu = True
        self.screen.fill(LIGHTGRAY)
        self.title = Text(self, "Winner!", 40,  WINDOW_WIDTH/2 - BUTTON_WIDTH/2 + 5, self.position )
        self.sub = Text(self, "Player: " + str(self.winner), 40,  WINDOW_WIDTH/2 - BUTTON_WIDTH/2, self.position + 100 )
        self.btnQuit = Button(self, "Quit", 7, WINDOW_WIDTH/2 - BUTTON_WIDTH/2, self.position + 350)
        while self.inMenu:
            self.clock.tick(FPS)
            self.screen.fill(LIGHTGRAY)
            self.widget_sprites.update()
            self.widget_sprites.draw(self.screen)
            for event in pg.event.get():
                self.updateWidgets(event)
                if event.type == pg.QUIT:
                    pg.quit()
            pg.display.flip()

g = Game()



g.show_start_screen()

while g.running:
    g.new()
    g.run()

g.show_go_screen()

pg.quit()
