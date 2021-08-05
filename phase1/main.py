#AI project part 1 Group 12
#Arman Hoseinmardi 97243021  Mohammad Mahdi peyravi 97243077

import random

def refreshPlayground():
    pg = []
    for i in range(8):
        pg.append([' '] * 8)

    return pg


def starter():
    return 'player'


def isOnPlayground(i, j):
    return 0 <= i <= 7 and 0 <= j <= 7


def resetPlayground(pg):
    for x in range(8):
        for y in range(8):
            pg[x][y] = ' '

    pg[4][3] = 'W'
    pg[4][4] = 'B'
    pg[3][3] = 'B'
    pg[3][4] = 'W'

def makeMove(board, tile, xstart, ystart):
    tilesToFlip = isLegalMove(board, tile, xstart, ystart)

    if not tilesToFlip:
        return False

    board[xstart][ystart] = tile
    for x, y in tilesToFlip:
        board[x][y] = tile
    return True


def drawPlayground(pg):
    print('    1   2   3   4   5   6   7   8')
    print('  --------------------------------- ')
    for y in range(8):
        print(y + 1, end=' ')
        for x in range(8):
            print('| %s' % (pg[x][y]), end=' ')
        print('|')
        print('  --------------------------------- ')


def isLegalMove(pg, t, xs, ys):
    if pg[xs][ys] != ' ' or not isOnPlayground(xs, ys):
        return False

    pg[xs][ys] = t

    if t == 'W':
        otherTile = 'B'
    else:
        otherTile = 'W'

    tilesToChange = []
    for xd, yd in [[0, 1], [1, 1], [1, 0], [1, -1], [0, -1], [-1, -1], [-1, 0], [-1, 1]]:
        x, y = xs, ys
        x += xd
        y += yd
        if isOnPlayground(x, y) and pg[x][y] == otherTile:
            x += xd
            y += yd
            if not isOnPlayground(x, y):
                continue
            while pg[x][y] == otherTile:
                x += xd
                y += yd
                if not isOnPlayground(x, y):
                    break
            if not isOnPlayground(x, y):
                continue
            if pg[x][y] == t:
                while True:
                    x -= xd
                    y -= yd
                    if x == xs and y == ys:
                        break
                    tilesToChange.append([x, y])

    pg[xs][ys] = ' '
    if len(tilesToChange) == 0:
        return not(len(tilesToChange) == 0)
    return tilesToChange


def getValidPlayground(playGround, tile):
    hint = duplicatePlayground(playGround)

    for x, y in getValidMoves(hint, tile):
        hint[x][y] = '*'
    return hint


def getValidMoves(board, tile):
    validMoves = []

    for x in range(8):
        for y in range(8):
            if isLegalMove(board, tile, x, y) != False:
                validMoves.append([x, y])
    return validMoves


def getScoreOfBoard(board):
    bscore = 0
    wscore = 0
    for x in range(8):
        for y in range(8):
            if board[x][y] == 'B':
                bscore += 1
            if board[x][y] == 'W':
                wscore += 1
    return {'B': bscore, 'W': wscore}


def enterPlayerTile():
    tile = ''
    while not (tile == 'B' or tile == 'W'):
        print('Choose B or W?')
        tile = input().upper()

    if tile == 'B':
        return ['B', 'W']
    else:
        return ['W', 'B']

def playAgain():
    print('play again? (y or n)')
    return input().lower().startswith('y')




def duplicatePlayground(board):
    dupeBoard = refreshPlayground()

    for x in range(8):
        for y in range(8):
            dupeBoard[x][y] = board[x][y]

    return dupeBoard


def isOnCorner(x, y):
    return (x == 0 and y == 0) or (x == 7 and y == 0) or (x == 0 and y == 7) or (x == 7 and y == 7)


def getPlayerMove(board, playerTile):
    DIGITS1TO8 = '1 2 3 4 5 6 7 8'.split()
    while True:
        print('Enter your move')
        move = input().lower()
        if move == 'quit':
            return 'quit'

        if len(move) == 2 and move[0] in DIGITS1TO8 and move[1] in DIGITS1TO8:
            y = int(move[0]) - 1
            x = int(move[1]) - 1
            if isLegalMove(board, playerTile, x, y) == False:
                print('Illegal move\nYou should only choose tiles with star (*)\nEnter 61 if y = 6 and x = 1')
            else:
                break
        else:
            print('Illegal move!. Type x and y between 1 to 8 ')
            print('example: 61  -> y = 6  x = 1')

    return [x, y]


def showPoints(playerTile, agentTile):
    scores = getScoreOfBoard(mainBoard)
    print('(You: %s   agent: %s)\n' % (scores[playerTile], scores[agentTile]))


def getAgentMove(board, agentTile):
    possibleMoves = getValidMoves(board, agentTile)

    random.shuffle(possibleMoves)

    for x, y in possibleMoves:
        if isOnCorner(x, y):
            return [x, y]

    bestScore = -1
    for x, y in possibleMoves:
        dupeBoard = duplicatePlayground(board)
        makeMove(dupeBoard, agentTile, x, y)
        score = getScoreOfBoard(dupeBoard)[agentTile]
        if score > bestScore:
            bestMove = [x, y]
            bestScore = score
    return bestMove




while True:
    mainBoard = refreshPlayground()
    resetPlayground(mainBoard)
    playerTile, agentTile = enterPlayerTile()
    showHints = True
    turn = starter()
    print('\nPlease start the game \nTutorial: Enter 63 for y = 6 and x = 3\nYou can only choose the tiles which has star(*)\n')

    while True:
        if turn == 'player':
            if showHints:
                validMovesBoard = getValidPlayground(mainBoard, playerTile)
                drawPlayground(validMovesBoard)
            showPoints(playerTile, agentTile)
            move = getPlayerMove(mainBoard, playerTile)
            if move != 'quit':
                makeMove(mainBoard, playerTile, move[0], move[1])

            if getValidMoves(mainBoard, agentTile) == []:
                break
            else:
                turn = 'agent'

        else:
            drawPlayground(mainBoard)
            showPoints(playerTile, agentTile)
            input('Press Enter for agent turn\n')
            x, y = getAgentMove(mainBoard, agentTile)
            makeMove(mainBoard, agentTile, x, y)

            if getValidMoves(mainBoard, playerTile) == []:
                break
            else:
                turn = 'player'


    drawPlayground(mainBoard)
    scores = getScoreOfBoard(mainBoard)
    print('B: %s  |  W: %s' % (scores['B'], scores['W']))
    if scores[playerTile] > scores[agentTile]:
        print('You won the game by %s points!' % (scores[playerTile] - scores[agentTile]))
    elif scores[playerTile] < scores[agentTile]:
        print('The agent won by %s points.' % (scores[agentTile] - scores[playerTile]))
    else:
        print('Tie game! No winner')

    if not playAgain():
        break