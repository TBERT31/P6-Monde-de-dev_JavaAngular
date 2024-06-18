export interface Comment {
    id: number;
    message: string;
    username: string;
    article_id: number;
    createdAt?: Date;
    updatedAt?: Date;
}
